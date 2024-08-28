import SwiftUI
import shared

struct ContentView: View {
    @State var text: String = ""
    @State var hide: Bool = true
    
	var body: some View {
        UIDraw.Background(ignoresSafeArea: true) { size in
            Text("")
        }
	}
}

#Preview {
    ContentView()
}
