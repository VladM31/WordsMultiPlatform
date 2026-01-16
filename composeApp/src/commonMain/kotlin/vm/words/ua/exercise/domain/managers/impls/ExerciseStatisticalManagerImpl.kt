package vm.words.ua.exercise.domain.managers.impls

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.utils.toPair
import vm.words.ua.exercise.domain.managers.ExerciseStatisticalManager
import vm.words.ua.exercise.domain.models.data.EndExerciseTransaction
import vm.words.ua.exercise.domain.models.data.StartExerciseTransaction
import vm.words.ua.exercise.domain.models.data.WordCompleted
import vm.words.ua.exercise.domain.models.enums.Exercise
import vm.words.ua.exercise.net.clients.ExerciseStatisticalClient
import vm.words.ua.exercise.net.responds.EndExerciseTransactionRequest
import vm.words.ua.exercise.net.responds.StartExerciseTransactionRequest
import vm.words.ua.exercise.net.responds.WordCompletedRequest
import vm.words.ua.learning.domain.models.enums.LearningHistoryType
import vm.words.ua.learning.net.clients.LearningHistoryClient
import vm.words.ua.learning.net.clients.LearningPlanClient
import vm.words.ua.learning.net.responds.LearningPlanResponse
import vm.words.ua.playlist.domain.managers.PlayListManager
import vm.words.ua.playlist.domain.models.PlayListGrade

class ExerciseStatisticalManagerImpl(
    private val client: ExerciseStatisticalClient,
    private val userManager: UserCacheManager,
    private val learningPlanClient: LearningPlanClient,
    private val learningHistoryClient: LearningHistoryClient,
    private val playListManager: PlayListManager
) : ExerciseStatisticalManager {
    private val gradeMapper = mapOf(
        Exercise.WORD_BY_TRANSLATES.id to 1L,
        Exercise.WORD_BY_ORIGINALS.id to 1L,
        Exercise.WORD_BY_DESCRIPTIONS.id to 1L,
        Exercise.DESCRIPTION_BY_WORDS.id to 1L,

        Exercise.MATCH_WORDS.id to 3L,
        Exercise.LETTERS_MATCH_BY_TRANSLATION.id to 3L,
        Exercise.LETTERS_MATCH_BY_DESCRIPTION.id to 3L,
        Exercise.WORD_BY_WRITE_TRANSLATE.id to 3L,
        Exercise.WORD_BY_WRITE_DESCRIPTION.id to 3L
    )


    override suspend fun startExercise(data: StartExerciseTransaction) {
        data.toRequest(
            plan = runCatching { learningPlanClient.getPlan(userManager.token.value) }.getOrNull(),
            countMap = runCatching {
                learningHistoryClient.getCount(userManager.token.value)
                    .content
                    .associate { it.type to it.count }
            }.getOrDefault(emptyMap())
        ).runCatching {
            client.startExercise(this, userManager.toPair())
        }.onFailure {
            println("ExerciseStatisticalManager - Failed to start exercise: ${it.message}")
        }
    }

    override suspend fun completeWord(data: WordCompleted) {
        data.toRequest().runCatching {
            client.completeWord(this, userManager.toPair())
        }.onFailure {
            println("ExerciseStatisticalManager - Failed to complete word: ${it.message}")
        }
        playListManager.runCatching {
            updateGrades(listOf(data.toPlayListGradeWord()))
        }
    }

    override suspend fun endExercise(data: EndExerciseTransaction) {
        data.toRequest().runCatching {
            client.endExercise(this, userManager.toPair())
        }.onFailure {
            println("ExerciseStatisticalManager - Failed to end exercise: ${it.message}")
        }
    }

    private fun StartExerciseTransaction.toRequest(
        countMap: Map<LearningHistoryType, Int> = emptyMap(),
        plan: LearningPlanResponse? = null
    ): StartExerciseTransactionRequest {
        return StartExerciseTransactionRequest(
            transactionId = this.transactionId,
            exercises = this.exercises,
            createdAt = this.createdAt,
            recommendationId = this.recommendationId,
            words = this.words.map { it.toRequest() },
            wordCount = StartExerciseTransactionRequest.WordCountRequest(
                addedToLearning = countMap[LearningHistoryType.CREATE] ?: 0,
                repetitions = countMap[LearningHistoryType.UPDATE] ?: 0
            ),
            learningPlan = plan?.toRequest()
        )
    }

    private fun LearningPlanResponse.toRequest(): StartExerciseTransactionRequest.LearningPlan {
        return StartExerciseTransactionRequest.LearningPlan(
            wordsPerDay = this.wordsPerDay,
            nativeLang = this.nativeLang.shortName,
            learningLang = this.learningLang.shortName,
            cefr = this.cefr.name,
            dateOfCreation = this.createdAt.toLocalDateTime(
                TimeZone.currentSystemDefault()
            ).toString()
        )
    }

    private fun StartExerciseTransaction.Word.toRequest(): StartExerciseTransactionRequest.WordRequest {
        return StartExerciseTransactionRequest.WordRequest(
            wordId = this.wordId,
            userWordId = this.userWordId,
            grade = this.grade,
            dateOfAdded = this.dateOfAdded,
            lastReadDate = this.lastReadDate,
            original = this.original,
            translate = this.translate,
            lang = this.lang,
            translateLang = this.translateLang,
            cefr = this.cefr,
            hasDescription = this.hasDescription,
            category = this.category,
            hasSound = this.hasSound,
            hasImage = this.hasImage
        )
    }

    private fun EndExerciseTransaction.toRequest(): EndExerciseTransactionRequest {
        return EndExerciseTransactionRequest(
            transactionId = this.transactionId,
            endedAt = this.endedAt
        )
    }

    private fun WordCompleted.toRequest(): WordCompletedRequest {
        return WordCompletedRequest(
            transactionId = this.transactionId,
            wordId = this.wordId,
            userWordId = this.userWordId,
            exerciseId = this.exerciseId,
            attempts = this.attempts,
            isCorrect = this.isCorrect,
            completedAt = this.completedAt
        )
    }


    private fun WordCompleted.toPlayListGradeWord(): PlayListGrade {
        val grade = gradeMapper.getOrElse(exerciseId) { 0 }

        return PlayListGrade(
            wordId = this.wordId,
            wordGrade = if (isCorrect) {
                (grade - attempts).coerceAtLeast(0)
            } else {
                0L
            }
        )
    }
}