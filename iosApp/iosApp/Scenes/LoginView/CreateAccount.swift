import SwiftUI
import shared

struct CreateAccount: View {
    private let createAccountView: Binding<Bool>
    private let rcFrameSize = CGSize(width: UIConfig.Login.companion.rcFrameSize.width,
                                     height: UIConfig.Login.companion.rcFrameSize.height)
    private let checkBoxSize = CGSize(width: UIConfig.Login.companion.checkBoxSize.width,
                                      height: UIConfig.Login.companion.checkBoxSize.height)
    @State private var emailAddress = ""
    @State private var password1 = ""
    @State private var password2 = ""
    @State private var hidePassword = true
    @State private var statusText = ""
    @State private var isChecked = false
    @FocusState private var textFieldFocus: Bool
    
    init (_ createAccountView: Binding<Bool>) {
        self.createAccountView = createAccountView
    }
    
    var body: some View {
        NavigationView {
            UIDraw.Background(ignoresSafeArea: true, content: { size in
                ZStack {
                    VStack(spacing: 10) {
                        UIDraw.image("textlogo", scale: 0.5, bigger: false)
                        UIDraw.text("アカウントを新規作成", color: .black)
                        UIDraw.text(statusText, color: .red, emptyDraw: false)
                        UIDraw.textField($emailAddress, rcFrameSize, $textFieldFocus, label: "メールアドレス")
                        UIDraw.secureField($password1, $hidePassword, rcFrameSize, $textFieldFocus, label: "パスワード")
                        UIDraw.secureField($password2, $hidePassword, rcFrameSize, $textFieldFocus, label: "パスワード(確認用)")
                        HStack(spacing: 0) {
                            UIDraw.text("利用規約", color: .textButton)
                            UIDraw.text("に同意する", color: .black)
                                .padding(.trailing, 20)
                            UIDraw.rcFrame(checkBoxSize, color: .white, radius: 7, onTapped: { isChecked.toggle() }) {
                                isChecked ? AnyView(Image(systemName: "checkmark").foregroundStyle(.black)) :
                                            AnyView(EmptyView())
                            }
                        }
                        UIDraw.rcFrame(rcFrameSize, color: .themeColor, onTapped: { request() }) {
                            UIDraw.text("リクエスト", color: .white)
                        }
                        UIDraw.text("ご不明な点がございましたら、お手数ですが、", color: .black)
                        UIDraw.text("CistusSystem@gmail.com", color: .textButton) {
                            UIPasteboard.general.string = "CistusSystem@gmail.com"
                            statusText = "クリップボードにコピーしました。"
                        }
                        UIDraw.text("までご連絡ください。", color: .black)
                            .toolbar {
                                ToolbarItem(placement: .keyboard) {
                                    Button("完了") {
                                        textFieldFocus = false
                                    }
                                }
                            }
                    }
                    UIDraw.Version()
                }
                .toolbar {
                    ToolbarItemGroup(placement: .topBarLeading) {
                        UIDraw.text("完了", color: .textButton) {
                            createAccountView.wrappedValue = false
                        }
                    }
                }
            }, onTapped: { textFieldFocus = false })

        }
    }
    
    private func request() {
        textFieldFocus = false
    }
    
}

#Preview {
    CreateAccount(.constant(true))
}
