import SwiftUI
import shared

struct EmailAddressView: View {
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
                    UIDraw.text("メールアドレスの変更", color: .black)
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
            }
        }
    }
    
    private func request() {
        textFieldFocus = false
        statusText = "情報を確認しています..."
        let emailAddressCheck = EmailAddress(emailAddress: emailAddress)
        guard emailAddressCheck.isValid() else {
            statusText = "不正なメールアドレスです。"
            return
        }
        FirebaseAuth.updateEmailAddress(emailAddressCheck.emailAddress()) { result in
            if let result = result {
                statusText = result
                return
            }
            statusText = "入力したメールアドレス宛に再設定メールを送信しました。"
        }
    }
    
}

#Preview {
    EmailAddressView()
}
