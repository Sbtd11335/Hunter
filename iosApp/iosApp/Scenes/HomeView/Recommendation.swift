import SwiftUI
import shared

final class Recommendation: UIDraw.TabItem {
    @ObservedObject private var drawSize: DrawSize
    let label = "おすすめ"
    let draw: AnyView
    
    init(_ drawSize: DrawSize) {
        self.drawSize = drawSize
        draw = AnyView(Draw(drawSize))
    }
    
    private struct Draw: View {
        @Environment(\.verticalSizeClass) private var verticalSizeClass
        @Environment(\.horizontalSizeClass) private var horizontalSizeClass
        @ObservedObject private var drawSize: DrawSize
        private func isTablet() -> Bool {
            verticalSizeClass == .regular && horizontalSizeClass == .regular
        }
        
        init(_ drawSize: DrawSize) {
            self.drawSize = drawSize
        }

        var body: some View {
            VStack(spacing: 10) {
                UIDraw.text("新着", color: .black, font: .largeTitle, style: "Bold")
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.leading)
                if (!isTablet()) {
                    GiftContents.attention(drawSize.width, drawSize.height, "dummygift_attention") {
                        ZStack {}
                    }
                    GiftContents.attention(drawSize.width, drawSize.height, "dummygift_attention") {
                        ZStack {}
                    }
                    GiftContents.attention(drawSize.width, drawSize.height, "dummygift_attention") {
                        ZStack {}
                    }
                }
                else {
                    HStack(spacing: 10) {
                        GiftContents.attention(drawSize.width, drawSize.height, "dummygift_attention", true) {
                            ZStack {}
                        }
                        GiftContents.attention(drawSize.width, drawSize.height, "dummygift_attention", true) {
                            ZStack {}
                        }
                        GiftContents.attention(drawSize.width, drawSize.height, "dummygift_attention", true) {
                            ZStack {}
                        }
                    }
                }
            }
        }
        

    }

}

#Preview {
    NavigationStack {
        ScrollView {
            Recommendation(DrawSize(Screen.companion.width,
                                    Screen.companion.height)).draw
        }
    }
}
