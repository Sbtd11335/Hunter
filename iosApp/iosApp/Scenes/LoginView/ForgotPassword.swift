import SwiftUI
import shared

struct ForgotPassword: View {
    private let forgotPasswordView: Binding<Bool>
    private let rcFrameSize = CGSize(width: UIConfig.Login.companion.rcFrameSize.width,
                                     height: UIConfig.Login.companion.rcFrameSize.height)
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
                    .frame(maxWidth: rcFrameSize.width)
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
        textFieldFocus = false
        statusText = "情報を確認しています..."
        let emailAddressCheck = EmailAddress(emailAddress: emailAddress)
        if (!emailAddressCheck.isValid()) {
            statusText = "不正なメールアドレスです。"
            return
        }
        FirebaseAuth.sendPasswordReset(emailAddress) { result in
            if let result = result {
                statusText = result
                return
            }
            statusText = "入力したメールアドレス宛に再設定用メールを送信しました。"
        }
    }
    
}

#Preview {
    ForgotPassword(.constant(true))
}
