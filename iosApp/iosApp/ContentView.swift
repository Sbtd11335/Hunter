import SwiftUI
import shared

final class ShareData: ObservableObject {
    @Published var sceneID: SceneID = .Boot
    @Published var notifications = [UIDraw.ListItem]()
    @Published var unreadNotifications = false
    @Published var messages = [MessageData]()
    @Published var unreadMessages = 0
    
    func clear() {
        notifications.removeAll()
        unreadNotifications = false
        messages.removeAll()
        unreadMessages = 0
    }
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
