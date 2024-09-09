import SwiftUI

struct Setting: View {
    private var auth = FirebaseAuth()
    private var accountList = [UIDraw.ListItem]()
    @State private var etcList = [UIDraw.ListItem]()
    @State private var showSignOutConfirm = false
    @ObservedObject private var shareDatas: ShareDatas
    
    init(_ shareDatas: ShareDatas) {
        self.shareDatas = shareDatas
        accountList.append(UIDraw.ListItem("ユーザーID", content: auth.currentUser()?.uid ?? "Unknown"))
        accountList.append(UIDraw.ListItem("メールアドレス", content: auth.currentUser()?.email ?? "Unknown",
                                           navigateTo: { UpdateEmailAddress() }))
        accountList.append(UIDraw.ListItem("パスワード", navigateTo: { UpdatePassword() }))
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
                        UIDraw.DrawList(etcList, header: "その他")
                    }
                }
                .clipped()
                .scrollContentBackground(.hidden)
            }
            .background {
                UIDraw.Background(ignoresSafeArea: true)
            }
        }
        .onAppear {
            guard etcList.isEmpty else { return }
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
