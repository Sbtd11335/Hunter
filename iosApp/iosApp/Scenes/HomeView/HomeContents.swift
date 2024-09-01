import SwiftUI
import shared

struct HomeContents: View {
    
    var body: some View {
        NavigationStack {
            TabView {
                Home().tabItem {
                    Label("ホーム", systemImage: "house")
                }
                History().tabItem {
                    Label("履歴", systemImage: "book")
                }
            }
            .tint(.themeColor)
            .toolbar {
                ToolbarItem(placement: .principal) {
                    Image("textlogo")
                        .resizable()
                        .scaledToFit()
                }
            }
        }
    }
}

#Preview {
    HomeContents()
}
