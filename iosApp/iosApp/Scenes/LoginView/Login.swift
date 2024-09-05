import SwiftUI
import shared

struct Login: View {
    private let appName = AppInfo.companion.appName
    private let version = AppInfo.companion.version
    private let build = AppInfo.companion.build
    private let rcFrameSize = CGSize(width: UIConfig.Login.companion.rcFrameSize.width,
                                     height: UIConfig.Login.companion.rcFrameSize.height)
    @State private var emailAddress = ""
    @State private var password = ""
    @State private var hidePassword = true
    @State private var forgotPasswordView = false
    @State private var createAccontView = false
    @State private var statusText = ""
    @FocusState private var textFieldFocus: Bool
    @ObservedObject private var shareDatas: ShareDatas
    
    init(_ shareDatas: ShareDatas) {
        self.shareDatas = shareDatas
    }

    var body: some View {
        UIDraw.Background(ignoresSafeArea: true, content: { size in
            ZStack {
                VStack(spacing: 10) {
                    UIDraw.image("textlogo", scale: 0.5, bigger: false)
                    UIDraw.text("アカウントにログイン", color: .black)
                    UIDraw.text(statusText, color: .red, emptyDraw: false)
                        .multilineTextAlignment(.center)
                    UIDraw.textField($emailAddress, rcFrameSize, $textFieldFocus, label: "メールアドレス")
                    UIDraw.secureField($password, $hidePassword, rcFrameSize, $textFieldFocus, label: "パスワード")
                    UIDraw.rcFrame(rcFrameSize, color: .themeColor, onTapped: { login() }) {
                        UIDraw.text("ログイン", color: .white)
                    }
                    UIDraw.text("パスワード忘れた場合", color: .textButton) {
                        forgotPasswordView = true
                        createAccontView = false
                    }
                    UIDraw.text("アカウントを新規作成", color: .textButton) {
                        forgotPasswordView = false
                        createAccontView = true
                    }
                }
                .frame(maxWidth: rcFrameSize.width)
                UIDraw.Version()
            }
            .fullScreenCover(isPresented: $forgotPasswordView,
                             content: { ForgotPassword($forgotPasswordView) })
            .fullScreenCover(isPresented: $createAccontView,
                             content: { CreateAccount($createAccontView) })
        }, onTapped: { textFieldFocus = false })
    }
    
    private func login() {
        textFieldFocus = false
        statusText = "情報を確認しています..."
        FirebaseAuth.signIn(emailAddress, password) { result in
            if let result = result {
                statusText = result
                return
            }
            shareDatas.sceneID = .Home
        }
        
    }
}

#Preview {
    Login(ShareDatas())
}
