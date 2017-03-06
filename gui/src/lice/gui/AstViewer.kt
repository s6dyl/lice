package lice.gui

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel
import lice.compiler.model.StringLeafNode
import lice.compiler.model.StringMiddleNode
import lice.compiler.model.StringNode
import lice.compiler.parse.buildNode
import lice.repl.VERSION_CODE
import java.awt.BorderLayout
import java.io.File
import javax.swing.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * map the ast
 */
private fun rec(
		node: StringNode,
		viewRoot: DefaultMutableTreeNode): DefaultMutableTreeNode {
	return when (node) {
		is StringLeafNode -> DefaultMutableTreeNode(node)
		is StringMiddleNode -> viewRoot.apply {
			node.list
					.subList(1, node.list.size)
					.forEachIndexed { index, stringNode ->
						insert(
								rec(stringNode, DefaultMutableTreeNode(stringNode)),
								index
						)
					}
		}
		else -> DefaultMutableTreeNode()
	}
}

fun createTreeFromFile(file: File): JTree {
	val ast = buildNode(file.readText())
	return JTree(rec(ast, DefaultMutableTreeNode(ast)))
}

/**
 * @author ice1000
 */
fun main(args: Array<String>) {
	UIManager.setLookAndFeel(WindowsLookAndFeel())
//	tree.isEditable = true
	val frame = JFrame("Lice language AST Viewer ${VERSION_CODE}")
	frame.layout = BorderLayout()
	frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
	frame.setLocation(80, 80)
	frame.setSize(480, 480)
	frame.add(
			JScrollPane(createTreeFromFile(File("sample/test9.lice"))),
			BorderLayout.CENTER
	)
	val button = JButton("Open ...")
	button.addActionListener { _ ->
		val f = JFileChooser()
		f.showDialog(frame, "Parse")
		f.selectedFile?.let {
			frame.add(createTreeFromFile(f.selectedFile))
		}
	}
	frame.add(button, BorderLayout.SOUTH)
	frame.isVisible = true
}