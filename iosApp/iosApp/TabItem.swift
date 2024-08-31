import SwiftUI

extension UIDraw {
    protocol TabItem {
        var label: String { get }
        var draw: AnyView { get }
    }
}
