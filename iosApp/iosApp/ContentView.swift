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
        case .Login: Login(shareData)
        case .Home: HomeContents(shareData)
        }
	}
}

#Preview {
    ContentView()
}
