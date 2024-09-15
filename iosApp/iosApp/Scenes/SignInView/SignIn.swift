import SwiftUI
import shared

struct SignIn: View {
    private let appName = AppInfo.companion.appName
    private let version = AppInfo.companion.version
    private let build = AppInfo.companion.build
    private let rcFrameSize = CGSize(width: UIConfig.SignIn.companion.rcFrameSize.width,
                                     height: UIConfig.SignIn.companion.rcFrameSize.height)
    @State private var emailAddress = ""
    @State private var password = ""
    @State private var hidePassword = true
    @State private var forgotPasswordView = false
    @State private var createAccontView = false
    @State private var statusText = ""
    @FocusState private var textFieldFocus: Bool
    @ObservedObject private var shareData: ShareData
    
    init(_ shareData: ShareData) {
        self.shareData = shareData
    }

    var body: some View {
        UIDraw.Background(ignoresSafeArea: true, content: { size in
            ZStack {
                VStack(spacing: 10) {
                    UIDraw.image("textlogo", scale: 0.5, bigger: false)
                    UIDraw.text("アカウントにサインイン", color: .black)
                    UIDraw.text(statusText, color: .red, emptyDraw: false)
                        .multilineTextAlignment(.center)
                        .frame(maxWidth: rcFrameSize.width)
                    UIDraw.textField($emailAddress, rcFrameSize, $textFieldFocus, label: "メールアドレス")
                    UIDraw.secureField($password, $hidePassword, rcFrameSize, $textFieldFocus, label: "パスワード")
                    UIDraw.rcFrame(rcFrameSize, color: .themeColor, onTapped: { signIn() }) {
                        UIDraw.text("サインイン", color: .white)
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
                UIDraw.Version()
            }
            .fullScreenCover(isPresented: $forgotPasswordView,
                             content: { ForgotPassword($forgotPasswordView) })
            .fullScreenCover(isPresented: $createAccontView,
                             content: { CreateAccount($createAccontView) })
        }, onTapped: { textFieldFocus = false })
    }
    
    private func signIn() {
        let auth = FirebaseAuth()
        textFieldFocus = false
        statusText = "情報を確認しています..."
        guard !emailAddress.isEmpty else {
            statusText = "メールアドレスを入力してください。"
            return
        }
        guard !password.isEmpty else {
            statusText = "パスワードを入力してください。"
            return
        }
        auth.signIn(emailAddress, password) { result in
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
                if let isEmailVerified = auth.isEmailVerified() {
                    if (!isEmailVerified) {
                        auth.sendEmailVerification { sendEmailResult in
                            if let sendEmailError = sendEmailResult {
                                switch(sendEmailError) {
                                case .networkError: statusText = String(localized: "NetworkError", table: "AuthErrorCodes")
                                case .invalidAPIKey: statusText = String(localized: "InvalidAPIKey", table: "AuthErrorCodes")
                                case .userNotFound: statusText = String(localized: "UserNotFound", table: "AuthErrorCodes")
                                default: statusText = String(localized: "Etc", table: "AuthErrorCodes")
                                }
                            }
                            else {
                                statusText = "入力されたメールアドレス宛に確認用メールを送信しました。"
                            }
                        }
                    }
                    else {
                        shareData.sceneID = .Home
                    }
                }
                else {
                    statusText = String(localized: "Etc", table: "AuthErrorCodes")
                }
            }
        }
    }
}

#Preview {
    SignIn(ShareData())
}
