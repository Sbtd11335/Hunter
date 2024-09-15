import SwiftUI
import shared

struct ForgotPassword: View {
    private let forgotPasswordView: Binding<Bool>
    private let rcFrameSize = CGSize(width: UIConfig.SignIn.companion.rcFrameSize.width,
                                     height: UIConfig.SignIn.companion.rcFrameSize.height)
    @State private var emailAddress = ""
    @State private var statusText = ""
    @FocusState private var textFieldFocus: Bool
    
    init (_ forgotPasswordView: Binding<Bool>) {
        self.forgotPasswordView = forgotPasswordView
    }
    
    var body: some View {
        NavigationView {
            UIDraw.Background(ignoresSafeArea: true, content: { size in
                ZStack {
                    VStack(spacing: 10) {
                        UIDraw.image("textlogo", scale: 0.5, bigger: false)
                        UIDraw.text("パスワードを再設定", color: .black)
                        UIDraw.text(statusText, color: .red, emptyDraw: false)
                            .multilineTextAlignment(.center)
                            .frame(maxWidth: rcFrameSize.width)
                        UIDraw.textField($emailAddress, rcFrameSize, $textFieldFocus, label: "メールアドレス")
                        UIDraw.rcFrame(rcFrameSize, color: .themeColor, onTapped: { request() }) {
                            UIDraw.text("リクエスト", color: .white)
                        }
                        UIDraw.text("ご不明な点がございましたら、お手数ですが、", color: .black)
                        UIDraw.text("CistusSystem@gmail.com", color: .textButton) {
                            UIPasteboard.general.string = "CistusSystem@gmail.com"
                            statusText = "クリップボードにコピーしました。"
                        }
                        UIDraw.text("までご連絡ください。", color: .black)
                    }
                    UIDraw.Version()
                }
                .toolbar {
                    ToolbarItemGroup(placement: .topBarLeading) {
                        UIDraw.text("完了", color: .textButton) {
                            forgotPasswordView.wrappedValue = false
                        }
                    }
                }
            }, onTapped: { textFieldFocus = false })

        }
    }
    
    private func request() {
        let auth = FirebaseAuth()
        textFieldFocus = false
        statusText = "情報を確認しています..."
        guard !emailAddress.isEmpty else {
            statusText = "メールアドレスを入力してください。"
            return
        }
        auth.sendPasswordReset(emailAddress) { result in
            if let error = result {
                switch(error) {
                case .networkError: statusText = String(localized: "NetworkError", table: "AuthErrorCodes")
                case .invalidAPIKey: statusText = String(localized: "InvalidAPIKey", table: "AuthErrorCodes")
                case .invalidEmail: statusText = String(localized: "InvalidEmail", table: "AuthErrorCodes")
                default: statusText = String(localized: "Etc", table: "AuthErrorCodes")
                }
            }
            else {
                statusText = "入力されたメールアドレス宛に再設定用メールを送信しました。"
            }
        }
    }
    
}

#Preview {
    ForgotPassword(.constant(true))
}
