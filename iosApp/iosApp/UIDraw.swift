import Foundation
import SwiftUI
import shared

final class DrawSize: ObservableObject {
    @Published var width: CGFloat
    @Published var height: CGFloat
    
    init(_ width: CGFloat = 0.0, _ height: CGFloat = 0.0) {
        self.width = width
        self.height = height
    }
}

final class UIDraw {
    struct Background: View {
        @ObservedObject private var drawSize = DrawSize()
        private let colors: [Color]
        private let content: ((DrawSize) -> any View)?
        private let ignoresSafeArea: Bool
        
        init(_ colors: [Color]? = nil, ignoresSafeArea: Bool = false, content: ((DrawSize) -> any View)? = nil) {
            self.colors = colors ?? [.appColor1, .appColor2]
            self.ignoresSafeArea = ignoresSafeArea
            self.content = content
        }
        
        var body: some View {
            if (!ignoresSafeArea) {
                LinearGradient(colors: colors, startPoint: .topLeading, endPoint: .bottomTrailing)
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
            }
            else {
                LinearGradient(colors: colors, startPoint: .topLeading, endPoint: .bottomTrailing)
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
                    .ignoresSafeArea(.container)
            }
        }
    }
    static func text(_ text: String, table: String? = nil, color: Color = .primary, font: Font? = nil,
                     style: String = "Default", onTapped: (() -> Void)? = nil) -> AnyView {
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
                        content: (() -> any View)? = nil, onTapped: (() -> Void)? = nil) -> AnyView {
        var ret = AnyView(RoundedRectangle(cornerRadius: radius)
            .frame(width: size.width, height: size.height)
            .foregroundStyle(color))
        if (content != nil) {
            ret = AnyView(ret.overlay{ AnyView(content!()) })
        }
        return onTapped == nil ? ret : AnyView(ret.onTapGesture{ onTapped!() })
    }
    static func textField(_ text: Binding<String>, _ size: CGSize = CGSize(width: 200, height: 40), radius: CGFloat = 15,
                          label: String = "", backgroundColor: Color = .white, foregroundColor: Color = .black,
                          labelColor: Color = .gray, textPadding: CGFloat = 10) -> some View {
        rcFrame(size, color: backgroundColor, radius: radius, content: {
            TextField(text: text, label: {
                UIDraw.text(label, color: labelColor)
            })
            .foregroundStyle(foregroundColor)
            .padding(textPadding)
        })
    }
    static func secureField(_ text: Binding<String>, _ hide: Binding<Bool>, _ size: CGSize = CGSize(width: 200, height: 40),
                            radius: CGFloat = 15, label: String = "", backgroundColor: Color = .white,
                            foregroundColor: Color = .black, labelColor: Color = .gray, textPadding: CGFloat = 10) -> some View {
        let drawContent = !hide.wrappedValue ? AnyView(TextField(text: text, label: {
            UIDraw.text(label, color: labelColor)
        })) : AnyView(SecureField(text: text, label: {
            UIDraw.text(label, color: labelColor)
        }))
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
