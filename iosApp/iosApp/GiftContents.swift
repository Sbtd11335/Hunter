import SwiftUI
import shared

final class GiftContents {
    static func attention(_ width: CGFloat, _ height: CGFloat, _ image: String, _ isTablet: Bool = false, _ navigateTo: () -> some View) -> some View {
        let screenSize = UISize(width: width, height: height)
        let frameSize = UIConfig.Home.companion.getAttentionFrameSize(deviceSize: screenSize, isTablet: isTablet)
        let bottomBarSize = UIConfig.Home.companion.getBottomBarFrame(frameSize: frameSize)
        return NavigationLink(destination: {
            navigateTo()
        }, label: {
            UIDraw.rcFrame(frameSize.toCGSize()) {
                ZStack {
                    Image(image)
                        .resizable()
                        .clipShape(RoundedRectangle(cornerRadius: 15))
                    Rectangle()
                        .foregroundStyle(.white.opacity(0.25))
                        .frame(width: bottomBarSize.width, height: bottomBarSize.height)
                        .frame(maxHeight: .infinity, alignment: .bottom)
                        .clipShape(RoundedRectangle(cornerRadius: 15), style: FillStyle())
                        .blur(radius: 3.0)
                }
            }
            .shadow(radius: 3, x: 3, y: 3)
        })
    }
    static func largeAttention(_ width: CGFloat, _ height: CGFloat, _ image: String, _ navigateTo: () -> some View) -> some View {
        let screenSize = UISize(width: width, height: height)
        let frameSize = UIConfig.Home.companion.getLargeAttentionFrameSize(deviceSize: screenSize)
        let bottomBarSize = UIConfig.Home.companion.getBottomBarFrame(frameSize: frameSize)
        return NavigationLink(destination: {
            navigateTo()
        }, label: {
            UIDraw.rcFrame(frameSize.toCGSize()) {
                ZStack {
                    Image(image)
                        .resizable()
                        .clipShape(RoundedRectangle(cornerRadius: 15))
                    Rectangle()
                        .foregroundStyle(.white.opacity(0.25))
                        .frame(width: bottomBarSize.width, height: bottomBarSize.height)
                        .frame(maxHeight: .infinity, alignment: .bottom)
                        .clipShape(RoundedRectangle(cornerRadius: 15), style: FillStyle())
                        .blur(radius: 3.0)
                }
            }
            .shadow(radius: 10)
        })
    }
}
