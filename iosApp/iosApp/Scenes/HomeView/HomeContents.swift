import SwiftUI
import shared

struct HomeContents: View {
    @ObservedObject private var shareData: ShareData
    @State private var tosUpdate: Bool
    @State private var showNotificationView = false
    
    init(_ shareData: ShareData) {
        let tabBar = UITabBarAppearance()
        tabBar.stackedLayoutAppearance.selected.iconColor = UIColor(Color.themeColor)
        tabBar.stackedLayoutAppearance.selected.titleTextAttributes = [.foregroundColor: UIColor(Color.themeColor)]
        tabBar.stackedLayoutAppearance.normal.iconColor = UIColor.gray
        tabBar.stackedLayoutAppearance.normal.titleTextAttributes = [.foregroundColor: UIColor.gray]
        tabBar.inlineLayoutAppearance.selected.iconColor = UIColor(Color.themeColor)
        tabBar.inlineLayoutAppearance.selected.titleTextAttributes = [.foregroundColor: UIColor(Color.themeColor)]
        tabBar.inlineLayoutAppearance.normal.iconColor = UIColor.gray
        tabBar.inlineLayoutAppearance.normal.titleTextAttributes = [.foregroundColor: UIColor.gray]
        tabBar.compactInlineLayoutAppearance.selected.iconColor = UIColor(Color.themeColor)
        tabBar.compactInlineLayoutAppearance.selected.titleTextAttributes = [.foregroundColor: UIColor(Color.themeColor)]
        tabBar.compactInlineLayoutAppearance.normal.iconColor = UIColor.gray
        tabBar.compactInlineLayoutAppearance.normal.titleTextAttributes = [.foregroundColor: UIColor.gray]
        UITabBar.appearance().standardAppearance = tabBar
        self.shareData = shareData
        tosUpdate = ToS.checkUpdate()
    }
    
    var body: some View {
        NavigationStack {
            TabView {
                Home()
                    .tabItem {
                        Label("ホーム", systemImage: "house.fill")
                    }
                    .padding(.top, 10)
                    .background(UIDraw.Background(ignoresSafeArea: true))
                History()
                    .tabItem {
                        Label("履歴", systemImage: "book.fill")
                    }
                    .padding(.top, 10)
                    .background(UIDraw.Background(ignoresSafeArea: true))
                Message()
                    .tabItem {
                        Label("チャット", systemImage: "message.fill")
                    }
                    .padding(.top, 10)
                    .background(UIDraw.Background(ignoresSafeArea: true))
                Setting(shareData)
                    .tabItem {
                        Label("設定", systemImage: "gearshape.fill")
                    }
                    .padding(.top, 10)
                    .background(UIDraw.Background(ignoresSafeArea: true))
            }
            .toolbar {
                ToolbarItem(placement: .navigation) {
                    Image("textlogo")
                        .resizable()
                        .scaledToFit()
                        .frame(height: 30)
                }
                ToolbarItem(placement: .topBarTrailing) {
                    Button {
                        showNotificationView = true
                    } label: {
                        Image(systemName: "bell")
                            .foregroundStyle(.black)
                            .overlay {
                                Circle()
                                    .frame(width: 10)
                                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topTrailing)
                                    .foregroundStyle(.red)
                                    .opacity(shareData.unreadNotifications ? 1 : 0)
                            }
                    }
                }
                
            }
        }
        .onAppear {
            let data1 = FirebaseDatabase.Data1()
            data1.getData1(shareData)
        }
        .fullScreenCover(isPresented: $tosUpdate) {
            ToS($tosUpdate)
        }
        .fullScreenCover(isPresented: $showNotificationView) {
            NotificationView($showNotificationView, shareData)
        }
    }
}

#Preview {
    HomeContents(ShareData())
}
