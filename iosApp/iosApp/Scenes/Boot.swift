import SwiftUI
import shared

struct Boot: View {
    private let appName = AppInfo.companion.appName
    private let version = AppInfo.companion.version
    private let build = AppInfo.companion.build
    private let textlogoMax = Screen.companion.smallerSize * 0.5
    @State private var logoAnimationStart = false
    @State private var loadStart = false
    @ObservedObject private var shareData: ShareData
    
    init(_ shareData: ShareData) {
        self.shareData = shareData
    }
    
    var body: some View {
        UIDraw.Background(ignoresSafeArea: true) { size in
            ZStack {
                VStack {
                    HStack {
                        UIDraw.image("logo", scale: 0.28, bigger: false)
                        UIDraw.image("textlogo", scale: 0.5, bigger: false)
                            .offset(x: logoAnimationStart ? 0 : textlogoMax)
                            .frame(width: !logoAnimationStart ? 0 : textlogoMax)
                            .clipped()
                    }
                    .animation(.interactiveSpring(response: 0.25, dampingFraction: 2),
                               value: logoAnimationStart)
                    UIDraw.hideHStack(!loadStart) {
                        UIDraw.progress(scale: 1.5, color: .black)
                    }
                }
                UIDraw.Version()
            }
        }
        .onAppear {
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.4) {
                logoAnimationStart = true
            }
            DispatchQueue.main.asyncAfter(deadline: .now() + 1.4) {
                loadStart = true
                login()
                loadEtc()
            }
        }
    }
    
    private func login() {
        let auth = FirebaseAuth()
        guard auth.currentUser() != nil else {
            shareData.sceneID = .Login
            return
        }
        auth.reload { result in
            if (result != nil) {
                shareData.sceneID = .Login
            }
            else {
                if let isEmailVerified = auth.isEmailVerified() {
                    if (!isEmailVerified) {
                        shareData.sceneID = .Login
                        return
                    }
                    else {
                        shareData.sceneID = .Home
                        return
                    }
                }
                shareData.sceneID = .Login
            }
        }
    }
    private func loadEtc() {

    }
}

#Preview {
    Boot(ShareData())
}
