import SwiftUI
import shared

struct ToS: View {
    private let close: Binding<Bool>?
    
    init(_ close: Binding<Bool>? = nil) {
        self.close = close
        let tos = LocalDataConfig.ToS()
        let tosDirectoryPath = tos.directoryPath
        let tosDirectory = Directory(path: tosDirectoryPath)
        let tosDateFilePath = tos.dateFilePath
        let tosDateFile = TextFile(path: tosDateFilePath)
        if (!tosDirectory.isExists()) {
            tosDirectory.create()
        }
        tosDateFile.write(contents: String(localized: "Date", table: "ToS"))
    }
    
    var body: some View {
        NavigationStack {
            ScrollView {
                progressLines(contents: String(localized: "ToS", table: "ToS"))
            }
            .toolbar {
                if let close = close {
                    ToolbarItem(placement: .topBarLeading) {
                        Button("閉じる") {
                            close.wrappedValue = false
                        }
                    }
                }
            }
            .navigationTitle("利用規約")
            .navigationBarTitleDisplayMode(.large)
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
                        UIDraw.text("　\(item). \(content)", style: "Bold")
                    }
                }
                else {
                    UIDraw.text("　\(line)")
                }
            }
            .frame(maxWidth: .infinity, alignment: .leading)
        }
    }
    
    static func checkUpdate() -> Bool {
        let tos = LocalDataConfig.ToS()
        let tosDateFilePath = tos.dateFilePath
        let tosDateFile = TextFile(path: tosDateFilePath)
        if let date = tosDateFile.read() {
            if (date != String(localized: "Date", table: "ToS")) {
                return true
            }
        }
        else {
            return true
        }
        return false
    }
}

#Preview {
    ToS()
}
