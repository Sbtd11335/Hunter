import SwiftUI
import shared

struct Home: View {
    @State private var tabScrollX: CGFloat = 0.0
    
    var body: some View {
        GeometryReader { geometry in
            let size = geometry.size.toDrawSize()
            let tabItems: [UIDraw.TabItem] = [Recommendation(size), Popularity(size), Expensive(size)]
            let topBarSize = CGSize(width: Int(geometry.size.width) / tabItems.count, height: 60)
            ZStack {
                NavigationStack {
                    VStack(spacing: 0) {
                        ScrollViewReader { reader in
                            ZStack {
                                LazyHStack(spacing: 0) {
                                    ForEach(0..<tabItems.count, id: \.self) { i in
                                        UIDraw.rcFrame(topBarSize, color: .transparent, radius: 0, onTapped: {
                                            withAnimation {
                                                reader.scrollTo(tabItems[i].label)
                                            }
                                        }) {
                                            UIDraw.text(tabItems[i].label, color: .black)
                                        }
                                    }
                                }
                                Rectangle()
                                    .frame(width: topBarSize.width, height: 4)
                                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .bottomLeading)
                                    .offset(x: tabScrollX / CGFloat(tabItems.count))
                                    .foregroundStyle(Color.themeColor)
                            }
                            .frame(width: size.width, height: 60)
                            ScrollView(.horizontal, showsIndicators: false) {
                                LazyHStack(spacing: 0) {
                                    ForEach(0..<tabItems.count, id: \.self) { i in
                                        ScrollView(showsIndicators: false) {
                                            tabItems[i].draw
                                                .frame(width: size.width)
                                        }
                                        .id(tabItems[i].label)
                                    }
                                }
                                .background {
                                    GeometryReader { geometry in
                                        Color.clear.onChange(of: geometry.frame(in: .named("TabScroll")).minX) {
                                            tabScrollX = -geometry.frame(in: .named("TabScroll")).minX
                                        }
                                    }
                                }
                            }
                            .scrollTargetBehavior(.paging)
                            .coordinateSpace(name: "TabScroll")
                        }
                    }
                    .background {
                        UIDraw.Background(ignoresSafeArea: true)
                    }
                }
            }
            .frame(width: .infinity, height: .infinity)
        }
    }
    
}

#Preview {
    Home()
}
