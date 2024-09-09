import SwiftUI
import shared

struct UpdatePassword: View {
    private let rcFrameSize = UIConfig.Login.companion.rcFrameSize
    @State private var statusText = ""

    var body: some View {
        UIDraw.Background(ignoresSafeArea: true) { _ in
            VStack(spacing: 10) {
                UIDraw.image("textlogo", scale: 0.5, bigger: false)
                UIDraw.text("パスワードの変更", color: .black)
                UIDraw.text(statusText, color: .red, emptyDraw: false)
                    .multilineTextAlignment(.center)
                    .frame(maxWidth: rcFrameSize.width)
                UIDraw.rcFrame(rcFrameSize.toCGSize(), color: .themeColor) { request() } content: {
                    UIDraw.text("リクエスト", color: .white)
                }
                UIDraw.text("ご不明な点がございましたら、お手数ですが、", color: .black)
                UIDraw.text("CistusSystem@gmail.com", color: .textButton) {
                    UIPasteboard.general.string = "CistusSystem@gmail.com"
                    statusText = "クリップボードにコピーしました。"
                }
                UIDraw.text("もしくはメッセージまでご連絡ください。", color: .black)
            }
        }
    }
    
    private func request() {
        let auth = FirebaseAuth()
        statusText = "情報を確認しています..."
        guard let currentUserEmailAddress = auth.currentUser()?.email else {
            statusText = String(localized: "UserNotFound", table: "AuthErrorCodes")
            return
        }
        auth.sendPasswordReset(currentUserEmailAddress) { result in
            if let error = result {
                switch(error) {
                case .networkError: statusText = String(localized: "NetworkError", table: "AuthErrorCodes")
                case .invalidAPIKey: statusText = String(localized: "InvalidAPIKey", table: "AuthErrorCodes")
                case .invalidEmail: statusText = String(localized: "InvalidEmail", table: "AuthErrorCodes")
                default: statusText = String(localized: "Etc", table: "AuthErrorCodes")
                }
            }
            else {
                statusText = "設定されたメールアドレス宛に変更用メールを送信しました。"
            }
        }
    }
    
}

#Preview {
    UpdatePassword()
}
