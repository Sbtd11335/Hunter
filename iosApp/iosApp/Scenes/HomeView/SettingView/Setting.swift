import SwiftUI
import shared

struct Setting: View {
    private var accountLists = [UIDraw.ListItem]()
    private var userData = FirebaseAuth.getCurrentUser()
    @State private var etcLists = [UIDraw.ListItem]()
    @State private var signOutSheet = false
    @State private var signOutAlert = false
    @State private var statusText = ""
    @ObservedObject private var shareDatas: ShareDatas
    
    init(_ shareDatas: ShareDatas) {
        self.shareDatas = shareDatas
        self.accountLists.append(UIDraw.ListItem("ユーザーID", content: userData?.uid))
        self.accountLists.append(UIDraw.ListItem("メールアドレス", content: userData?.email){ EmailAddressView() })
        self.accountLists.append(UIDraw.ListItem("パスワード"){ PasswordView() })

    }
    
    var body: some View {
        NavigationStack {
            VStack(spacing: 0) {
                UIDraw.empty()
                ScrollView(showsIndicators: false) {
                    UIDraw.text("設定", color: .black, font: .largeTitle, style: "Bold")
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding()
                    UIDraw.DrawList(accountLists, header: "アカウント")
                    UIDraw.DrawList(etcLists, header: "その他")
                }
                .clipped()
            }
            .background {
                UIDraw.Background(ignoresSafeArea: true)
            }
        }
        .onAppear() {
            DispatchQueue.main.async {
                if (etcLists.isEmpty) {
                    etcLists.append(UIDraw.ListItem("ログアウト", textColor: .red,
                                                    onTapped: { signOutSheet = true }))
                }
            }
        }
        .confirmationDialog("サインアウトしますか？", isPresented: $signOutSheet, titleVisibility: .visible) {
            Button("サインアウト", role: .destructive) {
                signOut()
            }
        } message: {
            Text("一部のデータが失われる可能性があります。")
        }
        .alert(statusText == "" ? "ログアウトしました。" : "ログアウトに失敗しました。", isPresented: $signOutAlert) {
            Button("OK") {
                shareDatas.sceneID = .Boot
            }
        } message: {
            Text(statusText)
        }
    }
    
    private func signOut() {
        statusText = ""
        FirebaseAuth.signOut { result in
            if let result = result {
                statusText = result
            }
            signOutAlert = true
        }
    }
    
}

#Preview {
    Setting(ShareDatas())
}
