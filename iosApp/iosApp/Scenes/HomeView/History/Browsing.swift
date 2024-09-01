import SwiftUI
import shared

final class Browsing: UIDraw.TabItem {
    @ObservedObject private var drawSize: DrawSize
    let label: String = "閲覧履歴"
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
                UIDraw.text("閲覧履歴", color: .black, font: .largeTitle, style: "Bold")
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.leading)
                GiftContents.history(drawSize.width, drawSize.height, "dummygift_icon") {
                    ZStack {}
                }
                GiftContents.history(drawSize.width, drawSize.height, "dummygift_icon") {
                    ZStack {}
                }
                GiftContents.history(drawSize.width, drawSize.height, "dummygift_icon") {
                    ZStack {}
                }
            }
            
        }
    }
    
}

#Preview {
    NavigationStack {
        ScrollView {
            Browsing(DrawSize(Screen.companion.width, Screen.companion.height)).draw
        }
    }
}
