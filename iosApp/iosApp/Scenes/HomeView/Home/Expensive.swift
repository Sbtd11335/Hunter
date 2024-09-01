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
        @ObservedObject var drawSize: DrawSize
        
        init(_ drawSize: DrawSize) {
            self.drawSize = drawSize
        }
        
        var body: some View {
            VStack(spacing: 10) {
                UIDraw.text("高額", color: .black, font: .largeTitle , style: "Bold")
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.leading)
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
            Expensive(DrawSize(Screen.companion.width,
                               Screen.companion.height)).draw
        }
    }
}
