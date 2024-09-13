import SwiftUI
import shared

final class ShareDatas: ObservableObject {
    @Published var sceneID: SceneID = .Boot
    @Published var notifications = [UIDraw.ListItem]()
    @Published var unreadNotifications = false
}

struct ContentView: View {
    @ObservedObject private var shareDatas = ShareDatas()
    
	var body: some View {
        switch(shareDatas.sceneID) {
        case .Boot: Boot(shareDatas)
        case .Login: Login(shareDatas)
        case .Home: HomeContents(shareDatas)
        }
	}
}

#Preview {
    ContentView()
}
