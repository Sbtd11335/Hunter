import SwiftUI
import shared

final class Expensive: UIDraw.TabItem {
    @ObservedObject var drawSize: DrawSize
    let label = "高額"
    let draw: AnyView
    
    init(_ drawSize: DrawSize) {
        self.drawSize = drawSize
        draw = AnyView(Draw(drawSize))
    }
    
    private struct Draw: View {
        @ObservedObject private var drawSize: DrawSize
        @Environment(\.verticalSizeClass) private var verticalSizeClass
        @Environment(\.horizontalSizeClass) private var horizontalSizeClass

        init(_ drawSize: DrawSize) {
            self.drawSize = drawSize
        }
        
        private func isTablet() -> Bool { verticalSizeClass == .regular && horizontalSizeClass == .regular }
        
        var body: some View {
            VStack(spacing: 10) {
                UIDraw.text("高額", color: .black, font: .largeTitle , style: "Bold")
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.leading)
                if (!isTablet()) {
                    GiftContents.attention(drawSize.width, drawSize.height, "dummygift_attention", isTablet()) {
                        ZStack {}
                    }
                }
                else {
                    HStack(spacing: 10) {
                        GiftContents.attention(drawSize.width, drawSize.height, "dummygift_attention", isTablet()) {
                            ZStack {}
                        }
                        UIDraw.empty()
                            .padding(.leading, UIConfig.Home.companion.getAttentionFrameSize(deviceSize: drawSize.toUISize(), isTablet: isTablet()).width)
                        UIDraw.empty()
                            .padding(.leading, UIConfig.Home.companion.getAttentionFrameSize(deviceSize: drawSize.toUISize(), isTablet: isTablet()).width)
                    }
                }

            }
        }
    }
    
}

#Preview {
    NavigationStack {
        ScrollView {
            Expensive(DrawSize(Screen.companion.width,
                               Screen.companion.height)).draw
        }
    }
}
