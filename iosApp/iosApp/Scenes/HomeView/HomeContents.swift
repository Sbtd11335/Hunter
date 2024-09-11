import SwiftUI
import shared

struct HomeContents: View {
    @ObservedObject private var shareDatas: ShareDatas
    @State private var tosUpdate: Bool
    
    init(_ shareDatas: ShareDatas) {
        let tabBar = UITabBarAppearance()
        tabBar.stackedLayoutAppearance.selected.iconColor = UIColor(Color.themeColor)
        tabBar.stackedLayoutAppearance.selected.titleTextAttributes = [.foregroundColor: UIColor(Color.themeColor)]
        tabBar.stackedLayoutAppearance.normal.iconColor = UIColor.gray
        tabBar.stackedLayoutAppearance.normal.titleTextAttributes = [.foregroundColor: UIColor.gray]
        UITabBar.appearance().standardAppearance = tabBar
        self.shareDatas = shareDatas
        tosUpdate = shareDatas.tosUpdate
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
            .toolbar {
                ToolbarItem(placement: .principal) {
                    Image("textlogo")
                        .resizable()
                        .scaledToFit()
                }
            }
        }
        .fullScreenCover(isPresented: $tosUpdate, content: {
            ToS(shareDatas, isFullScreen: true)
        })
        .onChange(of: shareDatas.tosUpdate, initial: true) {
            tosUpdate = shareDatas.tosUpdate
        }
    }
}

#Preview {
    HomeContents(ShareDatas())
}
