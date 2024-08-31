import SwiftUI
import shared

final class ShareDatas: ObservableObject {
    @Published var sceneID: SceneID = .Home
}

struct ContentView: View {
    @ObservedObject private var shareData = ShareDatas()
    
	var body: some View {
        switch(shareData.sceneID) {
        case .Boot: Boot()
        case .Login: Login()
        case .Home: HomeContents()
        }
	}
}

#Preview {
    ContentView()
}
