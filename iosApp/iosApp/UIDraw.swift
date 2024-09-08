import Foundation
import SwiftUI
import shared

final class UIDraw {
    struct ListItem {
        let text: String
        let content: String?
        let textColor: Color
        let navigateTo: (() -> AnyView)?
        let onTapped: (() -> Void)?
        
        init(_ text: String) {
            self.text = text
            content = nil
            textColor = .black
            navigateTo = nil
            onTapped = nil
        }
        init(_ text: String, content: String) {
            self.text = text
            self.content = content
            self.textColor = .black
            navigateTo = nil
            onTapped = nil
        }
        init(_ text: String, textColor: Color) {
            self.text = text
            content = nil
            self.textColor = textColor
            navigateTo = nil
            onTapped = nil
        }
        init(_ text: String, content: String, textColor: Color) {
            self.text = text
            self.content = content
            self.textColor = textColor
            navigateTo = nil
            onTapped = nil
        }
        init(_ text: String, content: String? = nil, textColor: Color? = nil,
             navigateTo: (() -> any View)? = nil) {
            self.text = text
            self.content = content
            self.textColor = textColor ?? .black
            self.navigateTo = navigateTo == nil ? nil : { AnyView(navigateTo!()) }
            self.onTapped = nil
        }
        init(_ text: String, content: String? = nil, textColor: Color? = nil,
             onTapped: (() -> Void)? = nil) {
            self.text = text
            self.content = content
            self.textColor = textColor ?? .black
            self.navigateTo = nil
            self.onTapped = onTapped
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
                                    .frame(maxWidth: size.width * 0.85)
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
    struct Background: View {
        @ObservedObject private var drawSize = DrawSize()
        private let colors: [Color]
        private let content: ((DrawSize) -> any View)?
        private let onTapped: (() -> Void)?
        private let ignoresSafeArea: Bool
        
        init(_ colors: [Color]? = nil, ignoresSafeArea: Bool = false,
             content: ((DrawSize) -> any View)? = nil, onTapped: (() -> Void)? = nil) {
            self.colors = colors ?? [.appColor1, .appColor2]
            self.ignoresSafeArea = ignoresSafeArea
            self.content = content
            self.onTapped = onTapped
        }
        
        var body: some View {
            onTapped == nil ? drawContent() : AnyView(drawContent().onTapGesture{ onTapped!() })
        }
        
        private func drawContent() -> AnyView {
            if (!ignoresSafeArea) {
                AnyView(LinearGradient(colors: colors, startPoint: .topLeading, endPoint: .bottomTrailing)
                    .background {
                        GeometryReader { geometry in
                            Color.clear.onChange(of: geometry.size.width, initial: true) {
                                drawSize.width = geometry.size.width
                                drawSize.height = geometry.size.height
                            }
                        }
                    }
                    .overlay {
                        if (content != nil) {
                            AnyView(content!(drawSize))
                        }
                    })
            }
            else {
                AnyView(LinearGradient(colors: colors, startPoint: .topLeading, endPoint: .bottomTrailing)
                    .background {
                        GeometryReader { geometry in
                            Color.clear.onChange(of: geometry.size.width, initial: true) {
                                drawSize.width = geometry.size.width
                                drawSize.height = geometry.size.height
                            }
                        }
                    }
                    .overlay {
                        if (content != nil) {
                            AnyView(content!(drawSize))
                        }
                    }
                    .ignoresSafeArea(.container))
            }
        }
        
    }
    struct Version: View {
        private let appName = AppInfo.companion.appName
        private let version = AppInfo.companion.version
        private let build = AppInfo.companion.build
        
        var body: some View {
            VStack {
                UIDraw.text(String(format: "%@ Version %@-%@",
                                   appName, version, build), color: .black)
                UIDraw.text("Developed by Cistus", color: .black, style: "Bold")
            }
            .frame(maxHeight: .infinity, alignment: .bottom)
            .padding(.bottom, 20)
        }
    }
    
    static func empty() -> some View { Text("").frame(width: 1, height: 1).opacity(0) }
    static func withId(_ id: String? = nil, content: () -> some View) -> AnyView {
        return id == nil ? AnyView(content()) : AnyView(content().id(id!))
    }
    static func text(_ text: String, table: String? = nil, color: Color = .primary, font: Font? = nil,
                     style: String = "Default", emptyDraw: Bool = true, onTapped: (() -> Void)? = nil) -> AnyView {
        if (!emptyDraw && text.isEmpty) {
            return AnyView(EmptyView())
        }
        var ret = AnyView(Text(String(localized: "\(text)", table: table))
            .foregroundStyle(color)
            .font(font))
        switch(style) {
        case "Bold": ret = AnyView(ret.bold())
        case "Italic": ret = AnyView(ret.italic())
        case "ItalicBold": ret = AnyView(ret.bold().italic())
        default: break
        }
        return onTapped == nil ? ret : AnyView(ret.onTapGesture { onTapped!() })
    }
    static func image(_ image: String, scale: CGFloat = 1.0, bigger: Bool? = nil, onTapped: (() -> Void)? = nil) -> AnyView {
        guard let img = UIImage(named: image) else {
            return AnyView(EmptyView())
        }
        let cgImg = img.cgImage!
        let width: CGFloat
        let height: CGFloat
        if (bigger == nil) {
            if (cgImg.height <= cgImg.width) {
                width = Screen.companion.smallerSize * scale
                height = Screen.companion.smallerSize * scale * CGFloat(cgImg.height) / CGFloat(cgImg.width)
            }
            else {
                width = Screen.companion.biggerSize * scale
                height = Screen.companion.biggerSize * scale * CGFloat(cgImg.height) / CGFloat(cgImg.width)
            }
        }
        else {
            if (!bigger!) {
                width = Screen.companion.smallerSize * scale
                height = Screen.companion.smallerSize * scale * CGFloat(cgImg.height) / CGFloat(cgImg.width)
            }
            else {
                width = Screen.companion.biggerSize * scale
                height = Screen.companion.biggerSize * scale * CGFloat(cgImg.height) / CGFloat(cgImg.width)
            }
        }
        let ret = AnyView(Image(image)
            .resizable()
            .aspectRatio(contentMode: .fill)
            .frame(width: width, height: height)
            .clipped())
        return onTapped == nil ? ret : AnyView(ret.onTapGesture{ onTapped!() })
    }
    static func progress(_ style: String = "Circular", scale: CGFloat = 1.0,
                         color: Color = .primary, onTapped: (() -> Void)? = nil) -> AnyView {
        var ret: AnyView
        switch(style) {
        case "Circular": ret = AnyView(ProgressView().progressViewStyle(.circular))
        case "Linrar": ret = AnyView(ProgressView().progressViewStyle(.linear))
        default: ret = AnyView(ProgressView().progressViewStyle(.circular))
        }
        ret = AnyView(ret
            .tint(color)
            .scaleEffect(scale))
        return onTapped == nil ? ret : AnyView(ret.onTapGesture{ onTapped!() })
    }
    static func rcFrame(_ size: CGSize = CGSize(width: 60, height: 60), color: Color = .primary, radius: CGFloat = 15,
                        shadow: CGFloat = 0, shadowX: CGFloat = 0, shadowY: CGFloat = 0, onTapped: (() -> Void)? = nil,
                        content: (() -> any View)? = nil) -> AnyView {
        var ret = AnyView(RoundedRectangle(cornerRadius: radius)
            .frame(width: size.width, height: size.height)
            .foregroundStyle(color)
            .shadow(radius: shadow, x: shadowX, y: shadowY))
        if (content != nil) {
            ret = AnyView(ret.overlay{ AnyView(content!()) })
        }
        return onTapped == nil ? ret : AnyView(ret.onTapGesture{ onTapped!() })
    }
    static func textField(_ text: Binding<String>, _ size: CGSize = CGSize(width: 200, height: 40),
                          _ focus: FocusState<Bool>.Binding? = nil, radius: CGFloat = 15, label: String = "",
                          backgroundColor: Color = .white, foregroundColor: Color = .black, labelColor: Color = .gray,
                          textPadding: CGFloat = 10, axis: Axis? = nil) -> some View {
        let textField = axis == nil ?
        TextField(text: text, label: {  UIDraw.text(label, color: labelColor) }) :
        TextField(text: text, axis: axis!, label: {  UIDraw.text(label, color: labelColor) })
        
        var drawContent = AnyView(textField
            .foregroundStyle(foregroundColor)
            .padding(textPadding))
        if (focus != nil) {
            drawContent = AnyView(drawContent.focused(focus!))
        }
        return rcFrame(size, color: backgroundColor, radius: radius, content: { drawContent })
    }
    static func secureField(_ text: Binding<String>, _ hide: Binding<Bool>, _ size: CGSize = CGSize(width: 200, height: 40),
                            _ focus: FocusState<Bool>.Binding? = nil, radius: CGFloat = 15, label: String = "",
                            backgroundColor: Color = .white, foregroundColor: Color = .black, labelColor: Color = .gray,
                            textPadding: CGFloat = 10) -> some View {
        var drawContent = !hide.wrappedValue ? AnyView(TextField(text: text, label: {
            UIDraw.text(label, color: labelColor)
        })) : AnyView(SecureField(text: text, label: {
            UIDraw.text(label, color: labelColor)
        }))
        if (focus != nil) {
            drawContent = AnyView(drawContent.focused(focus!))
        }
        return rcFrame(size, color: backgroundColor, radius: radius, content: {
            HStack(spacing: 0) {
                drawContent
                    .padding(textPadding)
                Image(systemName: !hide.wrappedValue ? "eye.slash" : "eye")
                    .padding(.trailing, textPadding)
                    .onTapGesture {
                        hide.wrappedValue.toggle()
                    }
            }
            .foregroundStyle(foregroundColor)
        })
    }
    static func hideVStack(_ hide: Bool, content: () -> some View) -> AnyView {
        AnyView(VStack {
            content()
        }
        .opacity(hide ? 0 : 1))
    }
    static func hideHStack(_ hide: Bool, content: () -> some View) -> AnyView {
        AnyView(HStack {
            content()
        }
        .opacity(hide ? 0 : 1))
    }
    static func hideZStack(_ hide: Bool, content: () -> some View) -> AnyView {
        AnyView(ZStack {
            content()
        }
        .opacity(hide ? 0 : 1))
    }
}
