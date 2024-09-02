import SwiftUI
import shared

final class DrawSize: ObservableObject {
    @Published var width: CGFloat
    @Published var height: CGFloat
    
    init(_ width: CGFloat = 0.0, _ height: CGFloat = 0.0) {
        self.width = width
        self.height = height
    }
    
    func toCGSize() -> CGSize {
        return CGSize(width: width, height: height)
    }
    
    func toUISize() -> UISize {
        return UISize(width: width, height: height)
    }
}

extension CGSize {
    func toDrawSize() -> DrawSize {
        return DrawSize(width, height)
    }
}
