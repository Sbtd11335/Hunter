import SwiftUI
import shared

extension UIDraw {
    struct ListItem {
        let text: String
        let content: String?
        let textColor: Color
        let navigateTo: (() -> AnyView)?
        let onTapped: (() -> Void)?
        let etc: Any?
        
        init(_ text: String, etc: Any? = nil) {
            self.text = text
            content = nil
            textColor = .black
            navigateTo = nil
            onTapped = nil
            self.etc = etc
        }
        init(_ text: String, content: String, etc: Any? = nil) {
            self.text = text
            self.content = content
            self.textColor = .black
            navigateTo = nil
            onTapped = nil
            self.etc = etc
        }
        init(_ text: String, textColor: Color, etc: Any? = nil) {
            self.text = text
            content = nil
            self.textColor = textColor
            navigateTo = nil
            onTapped = nil
            self.etc = etc
        }
        init(_ text: String, content: String, textColor: Color, etc: Any? = nil) {
            self.text = text
            self.content = content
            self.textColor = textColor
            navigateTo = nil
            onTapped = nil
            self.etc = etc
        }
        init(_ text: String, content: String? = nil, textColor: Color? = nil, etc: Any? = nil,
             navigateTo: (() -> any View)? = nil) {
            self.text = text
            self.content = content
            self.textColor = textColor ?? .black
            self.navigateTo = navigateTo == nil ? nil : { AnyView(navigateTo!()) }
            self.onTapped = nil
            self.etc = etc
        }
        init(_ text: String, content: String? = nil, textColor: Color? = nil, etc: Any? = nil,
             onTapped: (() -> Void)? = nil) {
            self.text = text
            self.content = content
            self.textColor = textColor ?? .black
            self.navigateTo = nil
            self.onTapped = onTapped
            self.etc = etc
        }
    }
    struct DrawList: View {
        private let listItems: [UIDraw.ListItem]
        private let header: String?
        @State private var size = CGSizeZero
        
        init(_ listItems: [UIDraw.ListItem], header: String? = nil) {
            self.listItems = listItems
            self.header = header
        }
        
        var body: some View {
            VStack(spacing: 0) {
                if let header = header {
                    UIDraw.text(header, color: .black, font: .system(size: 12))
                        .frame(maxWidth: size.width * 0.85, alignment: .leading)
                }
                LazyVStack(spacing: 0) {
                    ForEach(0..<listItems.count, id: \.self) { i in
                        VStack(spacing: 0) {
                            HStack {
                                UIDraw.text(listItems[i].text, color: listItems[i].textColor)
                                if (listItems[i].content != nil || listItems[i].navigateTo != nil) {
                                    Spacer()
                                }
                                if let content = listItems[i].content {
                                    UIDraw.text(content, color: .gray)
                                }
                                if (listItems[i].navigateTo != nil) {
                                    Image(systemName: "chevron.right")
                                        .foregroundStyle(.gray)
                                }
                            }
                            .padding(10)
                            .frame(maxWidth: size.width * 0.9)
                            .background {
                                if let onTapped = listItems[i].onTapped {
                                    Color.transparent.onTapGesture { onTapped() }
                                }
                                if let navigationTo = listItems[i].navigateTo {
                                    NavigationLink(destination: navigationTo()) {
                                        Color.transparent
                                    }
                                }
                            }
                            if (i != listItems.count - 1) {
                                Divider()
                                    .frame(width: size.width * 0.85)
                            }
                        }
                    }
                }
                .background {
                    ZStack {
                        GeometryReader { geometry in
                            Color.clear.onChange(of: geometry.size.width, initial: true) {
                                size = geometry.size
                            }
                        }
                        RoundedRectangle(cornerRadius: 15)
                            .foregroundStyle(.white)
                            .frame(maxWidth: size.width * 0.9)
                    }
                }
            }
        }
    }
    struct DrawNotificationBoard: View {
        private let listItems: [UIDraw.ListItem]
        @State private var size = CGSizeZero
        private let format: DateFormatter
        init(_ listItems: [UIDraw.ListItem]) {
            self.listItems = listItems
            format = DateFormatter()
            format.dateFormat = "YYYY/MM/dd HH:mm"
        }
        
        var body: some View {
            LazyVStack(spacing: 0) {
                ForEach(0..<listItems.count, id: \.self) { i in
                    ZStack {
                        VStack(alignment: .leading, spacing: 10) {
                            UIDraw.text(listItems[i].text, color: .black, style: "Bold")
                                .lineLimit(1)
                            if let content = listItems[i].content {
                                UIDraw.text(content, color: .black)
                                    .lineLimit(3)
                            }
                        }
                        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
                        if let date = (listItems[i].etc as? [String: Double])?["date"] {
                            let display = format.string(from: Date(timeIntervalSince1970: date))
                            UIDraw.text(display, color: .gray, font: .system(size: 12))
                                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .bottomLeading)
                        }
                    }
                    .padding(10)
                    .frame(maxWidth: size.width * 0.9)
                    .frame(height: Screen.companion.smallerSize / 3, alignment: .top)
                    if (i != listItems.count - 1) {
                        Divider()
                            .frame(width: size.width * 0.85)
                    }
                }
            }
            .background {
                RoundedRectangle(cornerRadius: 15)
                    .foregroundStyle(.white)
                    .frame(maxWidth: size.width * 0.9)
            }
            .background {
                GeometryReader { geometry in
                    Color.clear.onChange(of: geometry.size.width, initial: true) {
                        size = geometry.size
                    }
                }
            }
        }
    }
}

#Preview {
    let listItems = [UIDraw.ListItem("Test", content: "Hello"), UIDraw.ListItem("Test", content: "Hello")]
    return UIDraw.DrawNotificationBoard(listItems)
}
