import SwiftUI
import shared

final class ShareData: ObservableObject {
    @Published var sceneID: SceneID = .Boot
    @Published var notifications = [UIDraw.ListItem]()
    @Published var unreadNotifications = false
    @Published var messages = [MessageData]()
    @Published var unreadMessages = 0
    @Published var data3 = ShareDatabase.Data3()
    
    class ShareDatabase {
        class Data3 {
            var data1_new = 0
            var data1_count = 0
        }
    }
    
    func clear() {
        notifications.removeAll()
        unreadNotifications = false
        messages.removeAll()
        unreadMessages = 0
        data3 = ShareDatabase.Data3()
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
