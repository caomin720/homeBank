package system.homebank.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import system.homebank.entity.Menu;
import system.homebank.model.TreeNode;

public class MenuUtils
{
  public static String buildMenus(List<Menu> menuList)
  {
    
    if (menuList == null)
    {
      return null;
    }
    List<TreeNode> nodeList = new ArrayList<TreeNode>();
    for (Menu m : menuList)
    {
      TreeNode treeNode = new TreeNode(m.getId(), m.getMenuname(),m.getUrl(), m.getParentid());
      nodeList.add(treeNode);
    }
    Map<String,TreeNode> map = new HashMap<String,TreeNode>();
    for (TreeNode node : nodeList)
    {
      map.put(node.getId(), node);
    }
    List<TreeNode> ret = new ArrayList<TreeNode>();
    for (TreeNode node : nodeList)
    {
      String id = node.getId();
      String parentid = node.getParentid();
      if (parentid.compareTo(id) == 0 )
      {
        ret.add(node);
      }
      else
      {
        TreeNode parentNode = (TreeNode)map.get(parentid);
        if (parentNode == null)
        {
          throw new RuntimeException(String.format("节点%s的父节点%s没找到", node.getId(),parentid));
        }
        parentNode.addChild(node);
      }
    }

    return createHtml(ret);
  }
  private static String createHtml(List<TreeNode> list)
  {
    String result = "";
    String menu = "<div title=\"%s\" iconCls=\"icon-menu\"  style=\"padding:10px;\"><ul class=\"easyui-tree\" data-options='data:%s'></ul></div>";
    for (TreeNode o : list)
    {
      result = result + String.format(menu, new Object[] { o.getText(), JSONBinder.buildNormalBinder().toJson(o.getChildren()) });
    }
    return result;
  }
}
