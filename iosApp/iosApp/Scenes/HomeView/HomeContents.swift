import SwiftUI
import shared

struct HomeContents: View {
    @ObservedObject private var shareDatas: ShareDatas
    
    init(_ shareDatas: ShareDatas) {
        UITabBar.appearance().unselectedItemTintColor = UIColor.gray
        self.shareDatas = shareDatas
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
                Setting(shareDatas).tabItem {
                    Label("設定", systemImage: "gearshape.fill")
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
    HomeContents(ShareDatas())
}
