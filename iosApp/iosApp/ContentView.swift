import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerFactory().create()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

private extension Color {
    static let appBackground = Color(red: 0.07, green: 0.07, blue: 0.08) // ~#121315
}

struct ContentView: View {
    var body: some View {
        ZStack {
            Color.appBackground
                .ignoresSafeArea() // cover entire screen including notch
            ComposeView()
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
