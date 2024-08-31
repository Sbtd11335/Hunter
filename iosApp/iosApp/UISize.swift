import shared

extension UISize {
    func toCGSize() -> CGSize {
        return CGSize(width: self.width, height: self.height)
    }
}
