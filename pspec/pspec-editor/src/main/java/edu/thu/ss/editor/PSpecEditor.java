package edu.thu.ss.editor;

import static edu.thu.ss.editor.util.MessagesUtil.*;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;

import edu.thu.ss.editor.model.BaseModel;
import edu.thu.ss.editor.model.EditorModel;
import edu.thu.ss.editor.model.PolicyModel;
import edu.thu.ss.editor.model.VocabularyModel;
import edu.thu.ss.editor.util.EditorUtil;
import edu.thu.ss.editor.util.MessagesUtil;
import edu.thu.ss.editor.view.DataContainerView;
import edu.thu.ss.editor.view.OutputView;
import edu.thu.ss.editor.view.PolicyView;
import edu.thu.ss.editor.view.RuleView;
import edu.thu.ss.editor.view.UserContainerView;
import edu.thu.ss.editor.view.VocabularyView;
import edu.thu.ss.spec.global.CategoryManager;
import edu.thu.ss.spec.lang.parser.PolicyParser;
import edu.thu.ss.spec.lang.parser.PolicyWriter;
import edu.thu.ss.spec.lang.parser.VocabularyParser;
import edu.thu.ss.spec.lang.parser.VocabularyWriter;
import edu.thu.ss.spec.lang.parser.WritingException;
import edu.thu.ss.spec.lang.pojo.DataContainer;
import edu.thu.ss.spec.lang.pojo.Policy;
import edu.thu.ss.spec.lang.pojo.UserContainer;
import edu.thu.ss.spec.lang.pojo.Vocabulary;
import edu.thu.ss.spec.util.XMLUtil;

public class PSpecEditor {

	private Shell shell;

	private Display display;

	private EditorModel editorModel;

	private TreeItem vocabularyItems;

	private TreeItem policyItems;

	private Composite contentComposite;

	private Tree editorTree;

	private Map<String, MenuItem> menus = new HashMap<>();

	private Map<String, ToolItem> toolItems = new HashMap<>();

	private BaseModel editingModel;

	private OutputView outputView;

	/**
	 * Open the window.
	 */
	public void show() {
		editorModel = new EditorModel();
		initializeShell();
		initializeMenu();
		initializeToolbar();

		enableSave(false);

		initializeContent();

		shell.open();
		shell.layout();
		shell.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				Display.getDefault().dispose();
				System.exit(0);
			}
		});

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void initializeShell() {
		display = Display.getDefault();
		shell = new Shell();
		shell.setImage(SWTResourceManager.getImage(EditorUtil.Image_Logo));
		int defaultWidth = 1024;
		int defaultHeight = 768;
		shell.setMinimumSize(defaultWidth, defaultHeight);
		shell.setSize(defaultWidth, defaultHeight);
		EditorUtil.centerLocation(shell);
		shell.setText(MessagesUtil.getMessage(MessagesUtil.Editor_Name));
		shell.setLayout(new GridLayout());

		shell.addListener(SWT.Close, new Listener() {
			@Override
			public void handleEvent(Event event) {
				MessageBox mb = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
				mb.setText(getMessage(Confirm_Exit));
				mb.setMessage(getMessage(Confirm_Exit_Message));
				int ret = mb.open();
				if (ret == SWT.YES) {
					for (VocabularyModel model : editorModel.getVocabularies()) {
						save(model, false);
					}
					for (PolicyModel model : editorModel.getPolicies()) {
						save(model, false);
					}
				} else if (ret == SWT.NO) {
					//do nothing
				} else {
					event.doit = false;
				}
			}
		});

	}

	private void initializeMenu() {
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem fileItems = new MenuItem(menu, SWT.CASCADE);
		fileItems.setText(getMessage(File));

		Menu fileMenu = new Menu(fileItems);
		fileItems.setMenu(fileMenu);

		MenuItem newItems = new MenuItem(fileMenu, SWT.CASCADE);
		newItems.setText(getMessage(New));

		Menu newMenu = new Menu(newItems);
		newItems.setMenu(newMenu);

		MenuItem newVocab = new MenuItem(newMenu, SWT.NONE);
		newVocab.setText(getMessage(New_Vocabulary));
		menus.put(New_Vocabulary, newVocab);

		MenuItem newPolicy = new MenuItem(newMenu, SWT.NONE);
		newPolicy.setText(getMessage(New_Policy));
		menus.put(New_Policy, newPolicy);

		MenuItem openItems = new MenuItem(fileMenu, SWT.CASCADE);
		openItems.setText(getMessage(Open));

		Menu openMenu = new Menu(openItems);
		openItems.setMenu(openMenu);

		MenuItem openVocabulary = new MenuItem(openMenu, SWT.NONE);
		openVocabulary.setText(getMessage(Open_Vocabulary));
		menus.put(Open_Vocabulary, openVocabulary);

		MenuItem openPolicy = new MenuItem(openMenu, SWT.NONE);
		openPolicy.setText(getMessage(Open_Policy));
		menus.put(Open_Policy, openPolicy);

		MenuItem save = new MenuItem(fileMenu, SWT.NONE);
		save.setText(getMessage(Save));
		save.setImage(SWTResourceManager.getImage(EditorUtil.Image_Save));
		menus.put(Save, save);

		MenuItem saveAs = new MenuItem(fileMenu, SWT.NONE);
		saveAs.setText(getMessage(Save_As));
		saveAs.setImage(SWTResourceManager.getImage(EditorUtil.Image_Save_As));
		menus.put(Save_As, saveAs);

		MenuItem edit = new MenuItem(menu, SWT.CASCADE);
		edit.setText(getMessage(Edit));

		Menu editMenu = new Menu(edit);
		edit.setMenu(editMenu);

		MenuItem help = new MenuItem(menu, SWT.CASCADE);
		help.setText(getMessage(Help));

		Menu helpMenu = new Menu(help);
		help.setMenu(helpMenu);

		MenuItem about = new MenuItem(helpMenu, SWT.NONE);
		about.setText(getMessage(About));
		menus.put(About, about);

		newVocab.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Vocabulary vocabulary = new Vocabulary(editorModel.getNewVocabularyId());
				addVocabulary(vocabulary, "");
			}
		});

		newPolicy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Policy policy = new Policy(editorModel.getNewPolicyId());
				addPolicy(policy, "");
			}
		});
		openVocabulary.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openVocabulary();
			}
		});

		openPolicy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openPolicy();
			}
		});

		save.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				save(editingModel, false);
			}
		});

		saveAs.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				save(editingModel, true);
			};
		});
	}

	private void initializeToolbar() {
		ToolBar toolBar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);

		ToolItem openVocabulary = EditorUtil.newToolItem(toolBar, getMessage(Open_Vocabulary));
		toolItems.put(Open_Vocabulary, openVocabulary);

		ToolItem openPolicy = EditorUtil.newToolItem(toolBar, getMessage(Open_Policy));
		toolItems.put(Open_Policy, openPolicy);

		ToolItem save = EditorUtil.newToolItem(toolBar, getMessage(Save));
		save.setImage(SWTResourceManager.getImage(EditorUtil.Image_Save));
		toolItems.put(Save, save);

		ToolItem saveAs = EditorUtil.newToolItem(toolBar, getMessage(Save_As));
		saveAs.setImage(SWTResourceManager.getImage(EditorUtil.Image_Save_As));
		toolItems.put(Save_As, saveAs);

		openVocabulary.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openVocabulary();
			}
		});

		openPolicy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openPolicy();
			}
		});

		save.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				save(editingModel, false);
			}
		});

		saveAs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				save(editingModel, true);
			}
		});
	}

	private void initializeContent() {
		Composite composite = EditorUtil.newComposite(shell);
		composite.setLayout(new FillLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		SashForm mainForm = new SashForm(composite, SWT.NONE);

		editorTree = new Tree(mainForm, SWT.BORDER | SWT.V_SCROLL);
		vocabularyItems = EditorUtil.newTreeItem(editorTree, getMessage(Vocabulary));
		vocabularyItems.setImage(SWTResourceManager.getImage(EditorUtil.Image_Vocabular_Item));

		policyItems = EditorUtil.newTreeItem(editorTree, getMessage(Policy));
		policyItems.setImage(SWTResourceManager.getImage(EditorUtil.Image_Policy_Item));

		Vocabulary vocabulary = new Vocabulary(editorModel.getNewVocabularyId());
		addVocabulary(vocabulary, "");

		Policy policy = new Policy(editorModel.getNewPolicyId());
		addPolicy(policy, "");

		EditorUtil.processTree(editorTree);

		editorTree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				//TODO view switch
				for (Control sub : contentComposite.getChildren()) {
					sub.dispose();
				}

				editingModel = (BaseModel) item.getData();
				if (editingModel == null) {
					enableSave(false);
					return;
				}

				if (item.getParentItem() == vocabularyItems) {
					new VocabularyView(shell, contentComposite, SWT.NONE, (VocabularyModel) editingModel,
							item);
				} else if (item.getParentItem() == policyItems) {
					new PolicyView(shell, contentComposite, SWT.NONE, (PolicyModel) editingModel, item);
				} else if (item.getText().equals(getMessage(User_Container))) {
					new UserContainerView(shell, contentComposite, SWT.NONE, (VocabularyModel) editingModel);
				} else if (item.getText().equals(getMessage(Data_Container))) {
					new DataContainerView(shell, contentComposite, SWT.NONE, (VocabularyModel) editingModel);
				} else if (item.getText().equals(getMessage(Rule))) {
					new RuleView(shell, contentComposite, SWT.NONE, (PolicyModel) editingModel);
				}
				contentComposite.layout();

				enableSave(true);
			}
		});

		final SashForm contentForm = new SashForm(mainForm, SWT.VERTICAL);
		contentForm.setBackground(EditorUtil.getDefaultBackground());
		contentComposite = new Composite(contentForm, SWT.BORDER);
		contentComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		contentComposite.setLayout(new FillLayout());

		outputView = new OutputView(shell, contentForm, SWT.NONE);

		outputView.addWarning("Test warning 1", null, new Listener() {
			@Override
			public void handleEvent(Event event) {
				System.out.println(event);
			}
		});
		outputView.addError("Test error 1", null, new Listener() {
			@Override
			public void handleEvent(Event event) {
				System.out.println(event);
			}
		});
		outputView.refresh();
		mainForm.setWeights(new int[] { 1, 5 });
		contentForm.setWeights(new int[] { 5, 1 });
	}

	private void addVocabulary(Vocabulary vocabulary, String path) {
		VocabularyModel model = editorModel.addVocabulary(vocabulary, path);

		TreeItem item = EditorUtil.newTreeItem(vocabularyItems, vocabulary.getInfo().getId());
		item.setData(model);

		TreeItem userItem = EditorUtil.newTreeItem(item, getMessage(User_Container));
		userItem.setData(model);
		TreeItem dataItem = EditorUtil.newTreeItem(item, getMessage(Data_Container));
		dataItem.setData(model);

	}

	private void addPolicy(Policy policy, String path) {
		PolicyModel model = editorModel.addPolicy(policy, path);

		TreeItem item = EditorUtil.newTreeItem(policyItems, policy.getInfo().getId());
		item.setData(model);

		TreeItem ruleItem = EditorUtil.newTreeItem(item, getMessage(Rule));
		ruleItem.setData(model);

	}

	private void openVocabulary() {
		FileDialog dlg = EditorUtil.newOpenFileDialog(shell);
		String file = dlg.open();
		if (file != null) {
			if (editorModel.containVocabulary(file)) {
				EditorUtil.showMessageBox(shell, "", getMessage(Vocabulary_Opened_Message, file));
				return;
			}
			VocabularyParser parser = new VocabularyParser();
			try {
				Vocabulary vocabulary = parser.parse(file);
				addVocabulary(vocabulary, file);
			} catch (Exception e) {
				//TODO add check
				e.printStackTrace();
			}
		}
	}

	private void openPolicy() {
		FileDialog dlg = EditorUtil.newOpenFileDialog(shell);
		String file = dlg.open();
		if (file != null) {
			if (editorModel.containPolicy(file)) {
				EditorUtil.showMessageBox(shell, "", getMessage(Policy_Opened_Message, file));
				return;
			}
			PolicyParser parser = new PolicyParser();
			try {
				Policy policy = parser.parse(file);
				addPolicy(policy, file);
			} catch (Exception e) {
				//TODO add check
				e.printStackTrace();
			}
		}
	}

	private void enableSave(boolean enable) {
		menus.get(Save).setEnabled(enable);
		menus.get(Save_As).setEnabled(enable);
		toolItems.get(Save).setEnabled(enable);
		toolItems.get(Save_As).setEnabled(enable);
	}

	private void save(BaseModel model, boolean rename) {
		shell.setFocus();
		if (model instanceof VocabularyModel) {
			saveVocabulary((VocabularyModel) model, rename);
		} else if (model instanceof PolicyModel) {
			savePolicy((PolicyModel) model, rename);
		}

	}

	private void saveVocabulary(VocabularyModel model, boolean rename) {
		String path = model.getPath();
		String vocabularyId = model.getVocabulary().getInfo().getId();
		if (path.isEmpty() || rename) {
			FileDialog dlg = EditorUtil.newSaveFileDialog(shell, vocabularyId + ".xml");
			dlg.setText(getMessage(Save_Vocabulary, vocabularyId));
			path = dlg.open();
			if (path == null) {
				return;
			}
		}
		model.setPath(path);
		VocabularyWriter writer = new VocabularyWriter();
		try {
			//TODO
			writer.output(model.getVocabulary(), path);
			EditorUtil.showMessageBox(shell, "",
					getMessage(Vocabulary_Save_Success_Message, vocabularyId, path));
		} catch (WritingException e) {
			e.printStackTrace();
		}

	}

	private void savePolicy(PolicyModel model, boolean rename) {
		String path = model.getPath();
		String policyId = model.getPolicy().getInfo().getId();
		if (path.isEmpty() || rename) {
			FileDialog dlg = EditorUtil.newSaveFileDialog(shell, policyId + ".xml");
			dlg.setText(getMessage(Save_Policy, policyId));
			path = dlg.open();
			if (path == null) {
				return;
			}
		}
		model.setPath(path);
		PolicyWriter writer = new PolicyWriter();
		try {
			//TODO
			writer.output(model.getPolicy(), path);
			EditorUtil.showMessageBox(shell, "", getMessage(Policy_Save_Success_Message, policyId, path));
		} catch (WritingException e) {
			e.printStackTrace();
		}

	}
}