import SwiftUI

struct NotificationView: View {
    @State private var showNotificationView: Binding<Bool>
    @ObservedObject private var shareData: ShareData
    
    init(_ showNotificationView: Binding<Bool>, _ shareData: ShareData) {
        self.showNotificationView = showNotificationView
        self.shareData = shareData
        UINavigationBar.appearance().titleTextAttributes = [.foregroundColor: UIColor.black]
    }
    
    var body: some View {
        NavigationStack {
            VStack(spacing: 0) {
                UIDraw.empty()
                ScrollView {
                    if (shareData.notifications.isEmpty) {
                        UIDraw.text("お知らせはありません。", color: .black)
                            .frame(maxWidth: .infinity)
                    }
                    else {
                        UIDraw.DrawNotificationBoard(shareData.notifications)
                    }
                }
                .scrollContentBackground(.hidden)
            }
            .background(UIDraw.Background(ignoresSafeArea: true))
            .navigationTitle("お知らせ")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    Button {
                        showNotificationView.wrappedValue = false
                    } label: {
                        Text("閉じる")
                    }
                }
            }
            .onChange(of: shareData.unreadNotifications, initial: true) {
                FirebaseDatabase.Data1().getData1_1(shareData, true)
            }
        }
    }
}
