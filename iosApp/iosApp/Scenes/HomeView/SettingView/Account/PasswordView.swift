import SwiftUI
import shared

struct PasswordView: View {
    private let rcFrameSize = CGSize(width: UIConfig.Login.companion.rcFrameSize.width,
                                     height: UIConfig.Login.companion.rcFrameSize.height)
    @State private var emailAddress = ""
    @State private var statusText = ""
    @FocusState private var textFieldFocus: Bool
    
    var body: some View {
        UIDraw.Background(ignoresSafeArea: true) { size in
            ZStack {
                UIDraw.Version()
                VStack(spacing: 10) {
                    UIDraw.image("textlogo", scale: 0.5, bigger: false)
                    UIDraw.text("パスワードを再設定", color: .black)
                    UIDraw.text("設定されたメールアドレス宛に、変更用メールが送信されます。", color: .black)
                        .multilineTextAlignment(.center)
                    UIDraw.text(statusText, color: .red, emptyDraw: false)
                        .multilineTextAlignment(.center)
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
            }
        }
    }
    
    private func request() {
        textFieldFocus = false
        statusText = "情報を確認しています..."
        guard let userData = FirebaseAuth.getCurrentUser() else {
            statusText = "ユーザーが見つかりませんでした。"
            return
        }
        FirebaseAuth.sendPasswordReset(userData.email!) { result in
            if let result = result {
                statusText = result
                return
            }
            statusText = "設定されたメールアドレス宛に再設定用メールを送信しました。"
        }
    }
    
}

#Preview {
    PasswordView()
}
