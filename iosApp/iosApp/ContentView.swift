import SwiftUI
import shared

final class ShareData: ObservableObject {
    @Published var sceneID: SceneID = .Boot
    @Published var notifications = [UIDraw.ListItem]()
    @Published var unreadNotifications = false
}

struct ContentView: View {
    @ObservedObject private var shareData = ShareData()
    
	var body: some View {
        switch(shareData.sceneID) {
        case .Boot: Boot(shareData)
        case .SignIn: SignIn(shareData)
        case .Home: HomeContents(shareData)
        }
	}
}

#Preview {
    ContentView()
}
