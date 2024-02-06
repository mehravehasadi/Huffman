package huffman;


import java.util.*;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class HuffmanCoding {
	private static Map<Character, String> codes = new HashMap<>();
	private static Map<Character, Integer> freq = new HashMap<>();
	private static List<MinHeapNode> minHeap = new ArrayList<>();

	private static JTextArea resultTextArea;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> createAndShowGUI());
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("Huffman Coding");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 300);

		JTabbedPane tabbedPane = new JTabbedPane();

		JPanel encodePanel = createEncodePanel();
		tabbedPane.addTab("Encode", encodePanel);

		JPanel decodePanel = createDecodePanel();
		tabbedPane.addTab("Decode", decodePanel);

		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		resultTextArea = new JTextArea();
		resultTextArea.setEditable(false);
		JScrollPane resultScrollPane = new JScrollPane(resultTextArea);
		frame.getContentPane().add(resultScrollPane, BorderLayout.SOUTH);

		frame.setVisible(true);
	}

	private static JPanel createEncodePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JLabel inputLabel = new JLabel("Enter the string to be encoded:");
		JTextField inputTextField = new JTextField();
		JButton encodeButton = new JButton("Encode");

		encodeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String inputText = inputTextField.getText();
				resultTextArea.setText("Encoded Huffman data:");

				codes.clear();
				freq.clear();
				minHeap.clear();

				calcFreq(inputText);
				HuffmanCodes(inputText.length());

				StringBuilder encodedString = new StringBuilder();
				for (char c : inputText.toCharArray()) {
					encodedString.append(codes.get(c));
				}

				resultTextArea.append("\n" + encodedString.toString());
			}
		});

		panel.add(inputLabel);
		panel.add(inputTextField);
		panel.add(encodeButton);

		return panel;
	}

	private static JPanel createDecodePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JLabel inputLabel = new JLabel("Enter the string to be decoded:");
		JTextField inputTextField = new JTextField();
		JButton decodeButton = new JButton("Decode");

		decodeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String encodedText = inputTextField.getText();
				resultTextArea.setText("Decoded Huffman Data:");

				MinHeapNode root = minHeap.get(0);
				String decodedString = decodeFile(root, encodedText);

				resultTextArea.append("\n" + decodedString);
			}
		});

		panel.add(inputLabel);
		panel.add(inputTextField);
		panel.add(decodeButton);

		return panel;
	}

	private static void HuffmanCodes(int size) {
		for (char key : freq.keySet()) {
			minHeap.add(new MinHeapNode(key, freq.get(key)));
		}
		Collections.sort(minHeap);
		while (minHeap.size() != 1) {
			MinHeapNode left = minHeap.remove(0);
			MinHeapNode right = minHeap.remove(0);
			MinHeapNode top = new MinHeapNode('$', left.freq + right.freq);
			top.left = left;
			top.right = right;
			minHeap.add(top);
			Collections.sort(minHeap);
		}
		storeCodes(minHeap.get(0), "");
	}
	private static void calcFreq(String s) {
		for (char c : s.toCharArray()) {
			freq.put(c, freq.getOrDefault(c, 0) + 1);
		}
	}

	private static void storeCodes(MinHeapNode root, String s) {
		if (root == null) {
			return;
		}
		if (root.data != '$') {
			codes.put(root.data, s);
		}
		storeCodes(root.left, s + "0");
		storeCodes(root.right, s + "1");
	}

	private static String decodeFile(MinHeapNode root, String s) {
		StringBuilder ans = new StringBuilder();
		MinHeapNode curr = root;
		int n = s.length();
		for (int i = 0; i < n; i++) {
			if (s.charAt(i) == '0') {
				curr = curr.left;
			} else {
				curr = curr.right;
			}
			if (curr.left == null && curr.right == null) {
				ans.append(curr.data);
				curr = root;
			}
		}
		return ans.toString();
	}
}

class MinHeapNode implements Comparable<MinHeapNode> {
	char data;
	int freq;
	MinHeapNode left, right;

	MinHeapNode(char data, int freq) {
		this.data = data;
		this.freq = freq;
	}

	public int compareTo(MinHeapNode other) {
		return this.freq - other.freq;
	}
}