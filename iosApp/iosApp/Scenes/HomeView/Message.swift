import SwiftUI
import shared
import Combine

struct Message: View {
    @State private var message = ""
    @State private var currentMessageBoxSize: CGSize = CGSizeZero
    @FocusState private var textFieldFocus: Bool

    var body: some View {
        GeometryReader { screenSize in
            let messageFrameMaxWidth: CGFloat = screenSize.size.width * 0.8
            let messageBoxSize: CGSize = CGSize(width: screenSize.size.width * 0.8, height: 140)
            VStack(spacing: 0) {
                UIDraw.empty()
                ScrollViewReader { reader in
                    ScrollView {
                        VStack(spacing: 10) {
                            ForEach(0..<100, id: \.self) { i in
                                UIDraw.withId(i == 99 ? "Last" : nil) {
                                    drawMessage("Message", messageFrameMaxWidth, true)
                                }
                            }
                            .onAppear {
                                reader.scrollTo("Last")
                            }
                        }
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                    }
                }
                .clipped()
                .padding(.bottom, max(currentMessageBoxSize.height - 40, 0))
                .onTapGesture {
                    textFieldFocus = false
                }
                Spacer()
                drawMessageBox(messageBoxSize, "ここにメッセージを入力", 64) { currentMessageBoxSize in
                    self.currentMessageBoxSize = currentMessageBoxSize
                }
                .toolbar {
                    ToolbarItem(placement: .keyboard) {
                        Button("完了") {
                            textFieldFocus = false
                        }
                    }
                }
            }
        }
    }
    
    private func drawMessage(_ message: String, _ messageFrameMaxWidth: CGFloat,
                             _ fromUser: Bool = false) -> some View {
        let backColor = !fromUser ? Color.white : .themeColor
        let foreColor = !fromUser ? Color.black : .white
        let alignment = !fromUser ? Alignment.leading : .trailing
        let edge = !fromUser ? Edge.Set.leading : .trailing
        return UIDraw.text(message, color: foreColor)
            .padding(10)
            .background {
                RoundedRectangle(cornerRadius: 15)
                    .foregroundStyle(backColor)
            }
            .frame(maxWidth: messageFrameMaxWidth, alignment: alignment)
            .frame(maxWidth: .infinity, alignment: alignment)
            .padding(edge)
    }
    private func drawMessageBox(_ size: CGSize, _ label: String, _ maxChars: Int? = nil,
                                callback: @escaping (CGSize) -> Void) -> some View {
        HStack(spacing: 10) {
            TextField(text: $message, axis: .vertical) {
                UIDraw.text(label, color: .gray)
            }
            .onReceive(Just(message), perform: { _ in
                if (message.count >= maxChars ?? Int.max) {
                    message = String(message.prefix(maxChars ?? Int.max))
                }
            })
            
            .focused($textFieldFocus)
            .foregroundStyle(.black)
            .padding(10)
            .background {
                RoundedRectangle(cornerRadius: 15)
                    .foregroundStyle(.white)
                    .background {
                        GeometryReader { geometry in
                            Color.clear.onChange(of: geometry.size, initial: true) {
                                callback(geometry.size)
                            }
                        }
                    }
            }
            .frame(width: size.width)
            .frame(minHeight: size.height, alignment: .bottom)
            .frame(height: 40, alignment: .bottom)
            Image(systemName: "paperplane.fill")
                .foregroundStyle(.black)
                .onTapGesture { sendMessage() }
        }
        .padding(.bottom, 10)
    }
    private func sendMessage() {
        guard !message.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else {
            return
        }
        print(message)
        message = ""
        textFieldFocus = false
    }
}

#Preview {
    Message()
}
