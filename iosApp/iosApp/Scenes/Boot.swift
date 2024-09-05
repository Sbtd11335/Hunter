import SwiftUI
import shared

struct Boot: View {
    private let appName = AppInfo.companion.appName
    private let version = AppInfo.companion.version
    private let build = AppInfo.companion.build
    private let textlogoMax = Screen.companion.smallerSize * 0.5
    @State private var logoAnimationStart = false
    @State private var loadStart = false
    @ObservedObject private var shareDatas: ShareDatas
    
    init(_ shareDatas: ShareDatas) {
        self.shareDatas = shareDatas
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
            }
        }
    }
    
    private func login() {
        FirebaseAuth.isEmailVerified { result in
            guard let result = result else {
                shareDatas.sceneID = .Login
                return
            }
            if (!result) {
                shareDatas.sceneID = .Login
                return
            }
            shareDatas.sceneID = .Home
        }
    }
    
}

#Preview {
    Boot(ShareDatas())
}
