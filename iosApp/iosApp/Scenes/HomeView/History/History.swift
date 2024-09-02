import SwiftUI
import shared

struct History: View {
    @State private var tabScrollX: CGFloat = 0.0
    
    var body: some View {
        GeometryReader { geometry in
            let size = geometry.size.toDrawSize()
            let tabItems: [UIDraw.TabItem] = [Browsing(size), Application(size)]
            let topBarSize = CGSize(width: Int(geometry.size.width) / tabItems.count, height: 60)
            
            ZStack {
                NavigationStack {
                    VStack(spacing: 0) {
                        ScrollViewReader { reader in
                            ZStack {
                                HStack(spacing: 0) {
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
                            .frame(width: size.width, height: topBarSize.height)
                            ScrollView(.horizontal, showsIndicators: false) {
                                HStack(spacing: 0) {
                                    ForEach(0..<tabItems.count, id: \.self) { i in
                                        ScrollView(showsIndicators: false) {
                                            tabItems[i].draw
                                                .frame(width: size.width)
                                        }
                                        .id(tabItems[i].label)
                                    }
                                }
                                .background {
                                    GeometryReader { scrollGeometry in
                                        Color.clear.onChange(of: scrollGeometry.frame(in: .named("TabScroll")).minX) {
                                            tabScrollX = -scrollGeometry.frame(in: .named("TabScroll")).minX
                                        }
                                    }
                                }
                            }
                            .scrollTargetBehavior(.paging)
                            .coordinateSpace(name: "TabScroll")
                            .onChange(of: geometry.size.width) { beforeValue, _ in
                                reader.scrollTo(tabItems[Int(tabScrollX / beforeValue)].label)
                            }
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
    History()
}
