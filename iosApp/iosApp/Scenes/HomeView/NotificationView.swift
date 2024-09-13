import SwiftUI

struct NotificationView: View {
    @State private var showNotificationView: Binding<Bool>
    @ObservedObject private var shareDatas: ShareDatas
    
    init(_ showNotificationView: Binding<Bool>, _ shareDatas: ShareDatas) {
        self.showNotificationView = showNotificationView
        self.shareDatas = shareDatas
        UINavigationBar.appearance().titleTextAttributes = [.foregroundColor: UIColor.black]
    }
    
    var body: some View {
        NavigationStack {
            VStack(spacing: 0) {
                UIDraw.empty()
                ScrollView {
                    UIDraw.DrawNotificationBoard(shareDatas.notifications)
                }
                .scrollContentBackground(.hidden)
                .refreshable {
                    let data1 = FirebaseDatabase.Data1()
                    data1.getData1(shareDatas)
                }
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
            .onChange(of: shareDatas.unreadNotifications, initial: true) {
                FirebaseDatabase.Data1().getData1_1(shareDatas, true)
            }
        }
    }
}

#Preview {
    @ObservedObject var shareDatas = ShareDatas()
    
    return NotificationView(.constant(true), shareDatas)
        .onAppear {
            let calender = Calendar(identifier: .gregorian)
            let date = calender.date(from: DateComponents(year: 2024, month: 9, day: 13, hour: 13, minute: 12))!
            let format = DateFormatter()
            format.dateFormat = "YYYY/MM/dd HH:mm"
            let display = format.string(from: date)
            shareDatas.notifications.append(UIDraw.ListItem("Test", content: "サービス開始しました。", etc: ["date": display]))
        }
}
