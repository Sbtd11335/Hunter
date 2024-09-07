import SwiftUI
import shared

struct HomeContents: View {
    
    init() {
        UITabBar.appearance().unselectedItemTintColor = UIColor.gray
    }
    
    var body: some View {
        NavigationStack {
            TabView {
                Home().tabItem {
                    Label("ホーム", systemImage: "house.fill")
                }
                History().tabItem {
                    Label("履歴", systemImage: "book.fill")
                }
                Message().tabItem {
                    Label("チャット", systemImage: "message.fill")
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
