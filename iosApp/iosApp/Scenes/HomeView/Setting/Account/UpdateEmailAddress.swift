import SwiftUI
import shared
import FirebaseAuth

struct UpdateEmailAddress: View {
    private let rcFrameSize = UIConfig.Login.companion.rcFrameSize
    @State private var emailAddress: String = ""
    @State private var password: String = ""
    @State private var statusText: String = ""
    @State private var hidePassword = true
    @FocusState private var textFieldFocus: Bool
    
    var body: some View {
        UIDraw.Background(ignoresSafeArea: true) { _ in
            VStack(spacing: 10) {
                UIDraw.image("textlogo", scale: 0.5, bigger: false)
                UIDraw.text("メールアドレスの変更", color: .black)
                UIDraw.text(statusText, color: .red, emptyDraw: false)
                    .multilineTextAlignment(.center)
                    .frame(maxWidth: rcFrameSize.width)
                UIDraw.textField($emailAddress, rcFrameSize.toCGSize(), $textFieldFocus,
                                 label: "新しいメールアドレス")
                UIDraw.secureField($password, $hidePassword, rcFrameSize.toCGSize(), $textFieldFocus,
                                 label: "現在のパスワード")
                UIDraw.rcFrame(rcFrameSize.toCGSize(), color: .themeColor) { request() } content: {
                    UIDraw.text("リクエスト", color: .white)
                }
                UIDraw.text("ご不明な点がございましたら、お手数ですが、", color: .black)
                UIDraw.text("CistusSystem@gmail.com", color: .textButton) {
                    UIPasteboard.general.string = "CistusSystem@gmail.com"
                    statusText = "クリップボードにコピーしました。"
                }
                UIDraw.text("もしくはメッセージまでご連絡ください。", color: .black)
                    .toolbar {
                        ToolbarItem(placement: .keyboard) {
                            Button("完了") {
                                textFieldFocus = false
                            }
                        }
                    }
            }
        } onTapped: { textFieldFocus = false }
    }
    
    private func request() {
        let auth = FirebaseAuth()
        statusText = "情報を確認しています..."
        textFieldFocus = false
        guard let currentUserEmailAddress = auth.currentUser()?.email else {
            statusText = String(localized: "UserNotFound", table: "AuthErrorCodes")
            return
        }
        guard !emailAddress.isEmpty else {
            statusText = "メールアドレスを入力してください。"
            return
        }
        guard !password.isEmpty else {
            statusText = "パスワードを入力してください。"
            return
        }
        let authCredential = EmailAuthProvider.credential(withEmail: currentUserEmailAddress, password: password)
        auth.reauthenticate(authCredential) { result in
            if let error = result {
                switch(error) {
                case .networkError: statusText = String(localized: "NetworkError", table: "AuthErrorCodes")
                case .invalidAPIKey: statusText = String(localized: "InvalidAPIKey", table: "AuthErrorCodes")
                case .invalidEmail: statusText = String(localized: "InvalidEmail", table: "AuthErrorCodes")
                case .userDisabled: statusText = String(localized: "UserDisabled", table: "AuthErrorCodes")
                case .wrongPassword: statusText = String(localized: "WrongPassword", table: "AuthErrorCodes")
                case .invalidCredential: statusText = String(localized: "InvalidCredential", table: "AuthErrorCodes")
                default: statusText = String(localized: "Etc", table: "AuthErrorCodes")
                }
            }
            else {
                auth.updateEmailAddress(emailAddress) { updateEmailResult in
                    if let updateEmailError = updateEmailResult {
                        switch(updateEmailError) {
                        case .networkError: statusText = String(localized: "NetworkError", table: "AuthErrorCodes")
                        case .emailAlreadyInUse: statusText = String(localized: "EmailAlreadyInUse", table: "AuthErrorCodes")
                        case .invalidEmail: statusText = String(localized: "InvalidEmail", table: "AuthErrorCodes")
                        default: statusText = String(localized: "Etc", table: "AuthErrorCodes")
                        }
                    }
                    else {
                        statusText = "入力されたメールアドレス宛に認証用メールを送信しました。認証が完了したら自動でサインアウトされるため、再起動してください。"
                    }
                }
            }
        }
    }
    
}

#Preview {
    UpdateEmailAddress()
}
