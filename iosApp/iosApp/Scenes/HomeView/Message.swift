import SwiftUI
import shared
import Combine

struct Message: View {
    @State private var message = ""
    @State private var currentMessageBoxSize: CGSize = CGSizeZero
    @FocusState private var textFieldFocus: Bool
    @ObservedObject private var shareData: ShareData
    private var tabIndex: Binding<Int>
    
    init(_ shareData: ShareData, _ tabIndex: Binding<Int>) {
        self.shareData = shareData
        self.tabIndex = tabIndex
    }
    
    var body: some View {
        GeometryReader { screenSize in
            let messageFrameMaxWidth: CGFloat = screenSize.size.width * 0.9
            let messageBoxSize: CGSize = CGSize(width: screenSize.size.width * 0.8, height: 140)
            VStack(spacing: 0) {
                UIDraw.empty()
                ScrollViewReader { reader in
                    ScrollView {
                        if (shareData.messages.count == 0) {
                            UIDraw.text("メッセージはありません。", color: .black)
                        }
                        VStack(spacing: 10) {
                            ForEach(0..<shareData.messages.count, id: \.self) {
                                let messageData = shareData.messages[$0]
                                UIDraw.withId($0 == shareData.messages.count - 1 ? "lastMassage" : nil) {
                                    drawMessage(messageData, messageFrameMaxWidth)
                                }
                            }
                        }
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                    }
                    .onAppear {
                        reader.scrollTo("lastMassage")
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
        .onChange(of: shareData.unreadMessages, initial: true) {
            if (tabIndex.wrappedValue == 2) {
                let data3 = FirebaseDatabase.Data3()
                shareData.unreadMessages = 0
                data3.data1_edit(shareData)
            }
        }
    }
    
    private func drawMessage(_ messageData: MessageData, _ messageFrameMaxWidth: CGFloat,
                             displayTime: Bool = true) -> some View {
        let backColor = !messageData.fromUser ? Color.white : .themeColor
        let foreColor = !messageData.fromUser ? Color.black : .white
        let alignment = !messageData.fromUser ? Alignment.leading : .trailing
        let edge = !messageData.fromUser ? Edge.Set.leading : .trailing
        let md = messageData.toFormattedTime(format: "MM/dd")
        let hm = messageData.toFormattedTime(format: "HH:mm")
        let draw = UIDraw.text("\(md)\n\(hm)", color: .black, font: .system(size: 12))
            .opacity(displayTime ? 1 : 0)
        return HStack(alignment: .bottom) {
            if (messageData.fromUser) {
                draw
            }
            UIDraw.text(messageData.message, color: foreColor)
                .padding(10)
                .background {
                    RoundedRectangle(cornerRadius: 15)
                        .foregroundStyle(backColor)
                }
            if (!messageData.fromUser) {
                draw
            }
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
        let now = Date.now.timeIntervalSince1970
        let numberFormatter = NumberFormatter()
        numberFormatter.maximumFractionDigits = 50
        let date = numberFormatter.string(from: now * pow(10, 9) as NSNumber)!
        let messageData = MessageData(date: date, message: message, fromUser: true)
        let data3 = FirebaseDatabase.Data3()
        data3.setData1(messageData) {_ in}
        message = ""
        textFieldFocus = false
    }
}

#Preview {
    let shareData = ShareData()
    let message1 = MessageData(date: "\(1560000000 * pow(10, 9))", message: "Hello", fromUser: true)
    let message2 = MessageData(date: "\(1560000000 * pow(10, 9))", message: "Hello", fromUser: false)

    shareData.messages.append(message1)
    shareData.messages.append(message2)

    return Message(shareData, .constant(2))
        .background(.green)
}
