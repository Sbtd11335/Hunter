import SwiftUI
import shared

final class ShareDatas: ObservableObject {
    @Published var sceneID: SceneID = .Boot
}

struct ContentView: View {
    @State var text: String = ""
    @State var hide: Bool = true
    @ObservedObject var shareData = ShareDatas()
    
	var body: some View {
        switch(shareData.sceneID) {
        case .Boot: Boot()
        }
	}
}

#Preview {
    ContentView()
}
