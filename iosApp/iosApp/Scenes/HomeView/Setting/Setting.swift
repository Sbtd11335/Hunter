import SwiftUI
import shared

struct Setting: View {
    private let auth = FirebaseAuth()
    private var accountList = [UIDraw.ListItem]()
    private var appInfoList = [UIDraw.ListItem]()
    @State private var etcList = [UIDraw.ListItem]()
    @State private var showSignOutConfirm = false
    @ObservedObject private var shareDatas: ShareDatas
    
    init(_ shareDatas: ShareDatas) {
        self.shareDatas = shareDatas
        // Account
        accountList.append(UIDraw.ListItem("ユーザーID", content: auth.currentUser()?.uid ?? "Unknown"))
        accountList.append(UIDraw.ListItem("メールアドレス", content: auth.currentUser()?.email ?? "Unknown",
                                           navigateTo: { UpdateEmailAddress() }))
        accountList.append(UIDraw.ListItem("パスワード", navigateTo: { UpdatePassword() }))
        //AppInfo
        appInfoList.append(UIDraw.ListItem("バージョン", content: AppInfo.companion.version))
        appInfoList.append(UIDraw.ListItem("ビルド", content: AppInfo.companion.build))
        appInfoList.append(UIDraw.ListItem("利用規約", navigateTo: { ToS() }))
    }
    
    var body: some View {
        NavigationStack {
            VStack(spacing: 0) {
                UIDraw.empty()
                ScrollView {
                    VStack(spacing: 10) {
                        UIDraw.text("設定", color: .black, font: .largeTitle, style: "Bold")
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .padding(.leading)
                        UIDraw.DrawList(accountList, header: "アカウント")
                        UIDraw.DrawList(appInfoList, header: "アプリ情報")
                        UIDraw.DrawList(etcList, header: "その他")
                    }
                }
                .clipped()
                .scrollContentBackground(.hidden)
            }
        }
        .onAppear {
            guard etcList.isEmpty else { return }
            //Etc
            etcList.append(UIDraw.ListItem("サインアウト", textColor: .red, onTapped: {
                showSignOutConfirm = true
            }))
        }
        .confirmationDialog("サインアウトしますか?", isPresented: $showSignOutConfirm, titleVisibility: .visible,
                            actions: { Button("サインアウト", role: .destructive) { signOut() }
        }, message: {
            Text("一部のデータが失われる可能性があります。")
        })
    }
    
    private func signOut() {
        auth.signOut()
        shareDatas.sceneID = .Boot
    }
    
}

#Preview {
    Setting(ShareDatas())
}
