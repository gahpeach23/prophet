package app;

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.CardLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import backend.Database;
import backend.Transaction;
import components.Button;

import javax.swing.ImageIcon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;

public class App extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final ImageIcon appIcon = new ImageIcon(App.class.getResource("/icons/app.png"));
	private static final ImageIcon walletIcon = new ImageIcon(App.class.getResource("/icons/wallet.png"));
	private static final ImageIcon pieIcon = new ImageIcon(App.class.getResource("/icons/pie.png"));
	private static final ImageIcon logoutIcon = new ImageIcon(App.class.getResource("/icons/logout.png"));
	private static final ImageIcon pdfIcon = new ImageIcon(App.class.getResource("/icons/pdf.png"));
	
	private JPanel contentPane;
	private ArrayList<Transaction> transactions;
	private BigDecimal balance = new BigDecimal(0);
	private JLabel balanceLbl;
	private String username;
	private PiePage piePage;
	private JPanel pages;
	
	public App(String username) {
		this.username = username;

		setMinimumSize(new Dimension(1050, 850));
		setTitle("Prophet");
		setIconImage(appIcon.getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 810);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(null);

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel info = new JPanel();
		contentPane.add(info, BorderLayout.CENTER);
		info.setLayout(new BorderLayout());

		JPanel top = new JPanel() {
			public void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				Color c1 = new Color(120, 90, 140, 255); // light
				Color c2 = new Color(100, 24, 120, 255); // dark
				GradientPaint gp = new GradientPaint(0, 0, c2, getWidth(), getHeight(), c1);
				g2.setPaint(gp);
				g2.fill(new Rectangle(getWidth(), getHeight()));
			}
		};
		top.setPreferredSize(new Dimension(10, 40));
		top.setBackground(new Color(255, 168, 0));
		info.add(top, BorderLayout.NORTH);
		
		JLabel welcome = new JLabel("Welcome, " + username.toUpperCase() + "!");
		welcome.setBorder(new EmptyBorder(0, 10, 0, 0));
		welcome.setHorizontalAlignment(SwingConstants.LEFT);
		welcome.setForeground(new Color(255, 255, 255));
		welcome.setFont(new Font("Arial", Font.BOLD, 16));
		
		JLabel date = new JLabel(new SimpleDateFormat("EEEE, MMM dd YYYY").format(Calendar.getInstance().getTime()));
		date.setBorder(new EmptyBorder(0, 0, 0, 10));
		date.setHorizontalAlignment(SwingConstants.RIGHT);
		date.setForeground(Color.WHITE);
		date.setFont(new Font("Arial", Font.BOLD, 16));
		top.setLayout(new BorderLayout(0, 0));
		top.add(welcome, BorderLayout.WEST);
		top.add(date, BorderLayout.EAST);
		
		balanceLbl = new JLabel("Balance: $" + balance);
		balanceLbl.setHorizontalAlignment(SwingConstants.CENTER);
		balanceLbl.setForeground(Color.WHITE);
		balanceLbl.setFont(new Font("Dialog", Font.BOLD, 16));
		balanceLbl.setBorder(new EmptyBorder(0, 10, 0, 0));
		top.add(balanceLbl, BorderLayout.CENTER);
		
		pages = new JPanel();
		pages.setBorder(null);
		info.add(pages, BorderLayout.CENTER);
		CardLayout pagesLayout = new CardLayout();
		pages.setLayout(pagesLayout);
		
		piePage = new PiePage(Database.getTransactions(username));
		pages.add(piePage, "pie");
		refresh();
		WalletPage walletPage = new WalletPage(username, transactions, this);
		pages.add(walletPage, "wallet");
		
		JPanel nav = new JPanel() {
			public void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setPaint(new Color(40, 3, 50, 255));
				g2.fill(new Rectangle(getWidth(), getHeight()));
			}
		};
		nav.setBorder(null);
		nav.setBackground(new Color(0, 64, 128));
		nav.setPreferredSize(new Dimension(100, 380));
		contentPane.add(nav, BorderLayout.WEST);
		nav.setLayout(new BorderLayout(0, 0));

		JPanel pageBtns = new JPanel();
		pageBtns.setPreferredSize(new Dimension(10, 400));
		FlowLayout flowLayout = (FlowLayout) pageBtns.getLayout();
		flowLayout.setVgap(20);
		pageBtns.setBackground(new Color(0, 0, 0, 0));
		nav.add(pageBtns, BorderLayout.NORTH);
		
		JLabel walletBtn = new JLabel("");
		walletBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				pagesLayout.show(pages, "wallet");
			}
		});
		walletBtn.setIcon(new ImageIcon(walletIcon.getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH)));
		pageBtns.add(walletBtn);
		
		JLabel pieBtn = new JLabel("");
		pieBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				pagesLayout.show(pages, "pie");
			}
		});
		pieBtn.setIcon(new ImageIcon(pieIcon.getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH)));
		pageBtns.add(pieBtn);

		JPanel sysBtns = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) sysBtns.getLayout();
		sysBtns.setPreferredSize(new Dimension(10, 190));
		flowLayout_1.setVgap(20);
		sysBtns.setBackground(new Color(0, 0, 0, 0));
		nav.add(sysBtns, BorderLayout.SOUTH);
		
		JLabel pdfBtn = new JLabel("");
		pdfBtn.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				PdfDialog dialog = new PdfDialog(transactions, username);
				dialog.setModal(true);
				dialog.setVisible(true);
				if (!dialog.downloaded) {
					JOptionPane.showMessageDialog(getParent(), 
							"Download was cancelled and no action has been made.", 
							"Cancellation", 
							JOptionPane.INFORMATION_MESSAGE);
				} else {
						JOptionPane.showMessageDialog(getParent(), 
							"Download was successful and you may check your downloads folder!",
							"Success", 
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		pdfBtn.setIcon(new ImageIcon(pdfIcon.getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH)));
		sysBtns.add(pdfBtn);
		
		JLabel logout = new JLabel("");
		logout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
				Login login = new Login();
				login.setVisible(true);
			}
		});
		logout.setIcon(new ImageIcon(logoutIcon.getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH)));
		sysBtns.add(logout);
		
		pagesLayout.show(pages, "wallet");
	}
	
	public void refresh() {
		transactions = Database.getTransactions(username);
		balance = new BigDecimal(0);
		for (Transaction i : transactions) {
			balance = balance.add(i.getValue());
		}
		balanceLbl.setText("Balance: $" + new DecimalFormat("#,###.00").format(balance));
		pages.remove(piePage);
		piePage = new PiePage(transactions);
		pages.add(piePage, "pie");
	}
}
