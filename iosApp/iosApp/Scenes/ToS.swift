import SwiftUI
import shared

struct ToS: View {
    private let tosData: TextFile
    private let isFullScreen: Bool
    @ObservedObject private var shareDatas: ShareDatas
    
    init(_ shareDatas: ShareDatas, isFullScreen: Bool = false) {
        let tosDataFile = StorageConfig.Data1.companion.data1FilePath
        tosData = TextFile(path: tosDataFile)
        self.shareDatas = shareDatas
        self.isFullScreen = isFullScreen
    }
    
    var body: some View {
        NavigationStack {
            ScrollView {
                if let contents = tosData.read() {
                    progressLines(contents: contents)
                }
                else {
                    Text("利用規約を読み込めませんでした。")
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                }
            }
            .toolbar {
                if (isFullScreen) {
                    ToolbarItem(placement: .topBarLeading) {
                        Button("閉じる") {
                            shareDatas.tosUpdate = false
                        }
                    }
                }
            }
            .navigationTitle("利用規約")
        }
    }
    
    private func progressLines(contents: String) -> some View {
        let lines = contents.split(whereSeparator: \.isNewline)
        return VStack(spacing: 3) {
            ForEach(lines, id: \.self) { line in
                if let first = line.firstIndex(of: "*") {
                    let display = line[line.index(first, offsetBy: 1)..<line.lastIndex(of: "*")!]
                    UIDraw.text(String(display), font: .title2, style: "Bold")
                        .padding(.top, 10)
                        .padding(.leading, 10)
                }
                else if let first = line.firstIndex(of: "|") {
                    let last = line.lastIndex(of: "|")!
                    let item = line[line.index(first, offsetBy: 1)..<line.lastIndex(of: "|")!]
                    let content = line[line.index(last, offsetBy: 1)..<line.endIndex]
                    if (item.count == 0) {
                        UIDraw.text("　・\(content)")
                    }
                    else {
                        UIDraw.text("　\(item).\(content)")
                    }
                }
                else {
                    UIDraw.text("　\(line)")
                }
            }
            .frame(maxWidth: .infinity, alignment: .leading)
        }
    }
    
}

#Preview {
    ToS(ShareDatas())
}
