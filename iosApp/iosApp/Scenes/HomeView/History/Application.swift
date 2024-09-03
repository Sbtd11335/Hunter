import SwiftUI
import shared

final class Application: UIDraw.TabItem {
    @ObservedObject private var drawSize: DrawSize
    let label: String = "応募履歴"
    let draw: AnyView
    
    init(_ drawSize: DrawSize) {
        self.drawSize = drawSize
        draw = AnyView(Draw(drawSize))
    }
    
    private struct Draw: View {
        @ObservedObject private var drawSize: DrawSize
        @Environment(\.verticalSizeClass) var verticalSizeClass
        @Environment(\.horizontalSizeClass) var horizontalSizeClass
        
        init(_ drawSize: DrawSize) {
            self.drawSize = drawSize
        }
        
        private func isTablet() -> Bool { verticalSizeClass == .regular && horizontalSizeClass == .regular}
        
        var body: some View {
            VStack(spacing: 10) {
                UIDraw.text("応募履歴", color: .black, font: .largeTitle, style: "Bold")
                    .padding(.leading)
                    .frame(maxWidth: .infinity, alignment: .leading)
                if (!isTablet()) {
                    GiftContents.history(drawSize.width, drawSize.height, "dummygift_icon") {
                        ZStack {}
                    }
                }
                else {
                    HStack(spacing: 10) {
                        GiftContents.history(drawSize.width, drawSize.height, "dummygift_icon", isTablet()) {
                            ZStack {}
                        }
                        .padding(.trailing, UIConfig.History.companion.getContentFrameSize(deviceSize: drawSize.toUISize(), isTablet: true).width)
                        UIDraw.empty()
                    }
                }
            }
            
        }
    }
    
}

#Preview {
    NavigationStack {
        ScrollView {
            Application(DrawSize(Screen.companion.width, Screen.companion.height)).draw
        }
    }
}
