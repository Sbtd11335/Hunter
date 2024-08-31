import SwiftUI
import shared

final class Popularity: UIDraw.TabItem {
    @ObservedObject private var drawSize: DrawSize
    let label = "人気"
    let draw: AnyView
    
    init(_ drawSize: DrawSize) {
        self.drawSize = drawSize
        draw = AnyView(Draw(drawSize))
    }
    
    private struct Draw: View {
        @ObservedObject private var drawSize: DrawSize
        
        init(_ drawSize: DrawSize) {
            self.drawSize = drawSize
        }
        
        var body: some View {
            VStack(spacing: 10) {
                UIDraw.text("人気", color: .black, font: .largeTitle, style: "Bold")
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.leading)
                GiftContents.attention(drawSize.width, drawSize.height, "dummygift_attention") {
                    ZStack {}
                }
                GiftContents.attention(drawSize.width, drawSize.height, "dummygift_attention") {
                    ZStack {}
                }
            }
        }
    }
}

#Preview {
    NavigationStack {
        ScrollView {
            Popularity(DrawSize(Screen.companion.width,
                                Screen.companion.height)).draw
        }
    }
}
