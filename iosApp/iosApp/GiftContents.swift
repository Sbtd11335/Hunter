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
            UIDraw.rcFrame(frameSize.toCGSize(), shadow: 3, shadowX: 3, shadowY: 3) {
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
        })
    }
    static func largeAttention(_ width: CGFloat, _ height: CGFloat, _ image: String, _ navigateTo: () -> some View) -> some View {
        let screenSize = UISize(width: width, height: height)
        let frameSize = UIConfig.Home.companion.getLargeAttentionFrameSize(deviceSize: screenSize)
        let bottomBarSize = UIConfig.Home.companion.getBottomBarFrame(frameSize: frameSize)
        return NavigationLink(destination: {
            navigateTo()
        }, label: {
            UIDraw.rcFrame(frameSize.toCGSize(), shadow: 3, shadowX: 3, shadowY: 3) {
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
        })
    }
    static func history(_ width: CGFloat, _ height: CGFloat, _ image: String, _ isTablet: Bool = false, _ navigateTo: () -> some View) -> some View {
        let screenSize = UISize(width: width, height: height)
        let frameSize = UIConfig.History.companion.getContentFrameSize(deviceSize: screenSize, isTablet: isTablet)
        let imageSize = UIConfig.History.companion.getContentIconFrameSize(deviceSize: frameSize, isTablet: isTablet)
        return NavigationLink(destination: {
            navigateTo()
        }, label: {
            UIDraw.rcFrame(frameSize.toCGSize(), color: .white, shadow: 3, shadowX: 3, shadowY: 3) {
                HStack(spacing: 5) {
                    Image(image)
                        .resizable()
                        .scaledToFit()
                        .clipShape(RoundedRectangle(cornerRadius: 15))
                    VStack(alignment: .leading, spacing: 5) {
                        UIDraw.text("ダミー", color: .black, style: "Bold")
                        UIDraw.text("ダミーテキスト", color: .black)
                        Spacer()
                    }
                    
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(10)
            }
            .overlay {
                RoundedRectangle(cornerRadius: 15)
                    .stroke(lineWidth: 1.0)
                    .foregroundStyle(.black)
            }
        })
    }
}
