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

        init(_ drawSize: DrawSize) {
            self.drawSize = drawSize
        }
        
        var body: some View {
            VStack(spacing: 10) {
                UIDraw.text("応募履歴", font: .largeTitle, style: "Bold")
                    .padding(.leading)
                    .frame(maxWidth: .infinity, alignment: .leading)
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
            Application(DrawSize(Screen.companion.width, Screen.companion.height)).draw
        }
    }
}
